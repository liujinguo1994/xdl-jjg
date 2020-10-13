package com.xdl.jjg.web.controller.wap.member;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.oss.upload.OSSUploader;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.common.util.IPUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.model.domain.vo.EsCompanyVO;
import com.shopx.member.api.model.domain.vo.EsMemberVO;
import com.shopx.member.api.service.IEsCompanyService;
import com.shopx.member.api.service.IEsMemberLevelConfigService;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.system.api.model.domain.dto.EsSmsSendDTO;
import com.shopx.system.api.model.enums.SmsTemplateCodeEnum;
import com.shopx.system.api.service.IEsSmsService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.model.domain.vo.EsCheckPasswordVO;
import com.shopx.trade.api.service.IEsOrderService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.*;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * 会员表 前端控制器
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-24
 */
@Api(value = "/wap/registerMember", tags = "移动端-会员注册模块")
@RestController
@RequestMapping("/wap/registerMember")
public class EsWapMemberRegisterController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService iesMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsCompanyService iEsCompanyService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
    private IEsOrderService iEsOrderService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberLevelConfigService esMemberLevelConfigService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSmsService esSmsService;

    @Autowired
    private OSSUploader uploader;

    @Autowired
    private Producer producer;

    @Autowired
    private JedisCluster jedisCluster;

    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;

    //短信验证码长度
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    //短信失效时间
    @Value("${zhuox.shiro.member.sms.expire}")
    private int SMSEXPIRE;

    //验证码过期时间(5分钟)
    @Value("${zhuox.redisCode.expire}")
    private int CODEEXPIRE;


    @ApiOperation(value = "获取图片验证码", notes = "获取图片验证码")
    @GetMapping("/captcha.jpg")
    public void kaptcha(HttpServletResponse response, EsWapCairForm form) throws IOException {

        String type=form.getType();
        String uuid=form.getUuid();
        //生成文字验证码
        String code = producer.createText();
        if(StringUtils.isBlank(type) || "undefined".equals(type)){
            jedisCluster.setex(uuid, CODEEXPIRE, code);
        }else{
            switch (type){
                case "MODIFY_PASSWORD":
                    jedisCluster.setex("passWordCode" + "_" + uuid, CODEEXPIRE, code);
                    break;
                case "VALIDATE_MOBILE":
                case "BIND_MOBILE":
                case "FIND_PASSWORD":
                    jedisCluster.setex("findPassWordCode" + "_" + uuid, CODEEXPIRE, code);
                    break;
                case "REGISTER":
                    jedisCluster.setex(uuid, CODEEXPIRE, code);
                    break;
            }
        }

        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");

        BufferedImage image = producer.createImage(code);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        IOUtils.closeQuietly(outputStream);
    }


    @ApiOperation(value = "会员注册", notes = "依据前端传递的表单值新增会员")
    @PostMapping("/wapInsertMember")
    @ResponseBody
    public ApiResponse wapInsertMember(@RequestBody EsMemberRegistorForm esMemberForm, HttpServletRequest request) {
        //判断手机号格式
        if(!FormatValidateUtils.isMobile(esMemberForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        String mobileCode = jedisCluster.get("wap_member" + esMemberForm.getMobile());
        logger.info("session中短信码字符串:" + mobileCode);
        if(!StringUtil.isEmpty(esMemberForm.getCode()) && !esMemberForm.getCode().equals(mobileCode)){
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }
        //获取用户ip地址
        String ip = IPUtil.getIpAddr(request);
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(esMemberForm, esMemberDTO);
        if (null != ip) {
            esMemberDTO.setRegisterIp(ip);
        }
        //判断手机号是否已注册
        DubboResult<EsMemberDO> resultMobile = iesMemberService.getMemberInfoByMobile(esMemberForm.getMobile());
        if (resultMobile.isSuccess()) {
            if (resultMobile.getData() != null) {
                return ApiResponse.fail(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
            }
        }

        //判断用户名是否已被使用
        DubboResult<EsMemberDO> resultName = iesMemberService.getMemberInfoByName(esMemberForm.getName());
        if (resultName.isSuccess()) {
            if (resultName.getData() != null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_NAME_EXIST.getErrorCode(), MemberErrorCode.MEMBER_NAME_EXIST.getErrorMsg());
            }
        }

        //盐 加密
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        String passWord = ShiroKit.md5(esMemberDTO.getPassword(), esMemberDTO.getName() + salt);
        esMemberDTO.setPassword(passWord);
        esMemberDTO.setSalt(salt);
        DubboResult<EsMemberDO> result = iesMemberService.insertMember(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del("wap_member" + esMemberForm.getMobile());
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "查询手机号或者用户名是否被注册", notes = "根据url获取")
    @GetMapping(value = "/queryRepeatMobile/{nameOrMobile}")
    @ResponseBody
    @ApiImplicitParam(name = "nameOrMobile", value = "姓名或手机号", dataType = "string", paramType = "path", required = true, example = "1")
    public ApiResponse selectRepeatMobile(@PathVariable("nameOrMobile") String nameOrMobile) {
        DubboResult result = iesMemberService.getMemberInfoByNameOrMobile(nameOrMobile);
        if (result.isSuccess()) {
            EsMemberVO esMemberMobileVO = new EsMemberVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberMobileVO);
            }
            return ApiResponse.success(esMemberMobileVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "判断公司标识符是否合法")
    @GetMapping(value = "/queryCompanyByNotes/{notes}")
    @ResponseBody
    @ApiImplicitParam(name = "notes", value = "公司标识符", dataType = "string", paramType = "path", required = true, example = "1")
    public ApiResponse selectCompanyBynotes(@PathVariable("notes") String notes) {
        DubboResult result = iEsCompanyService.getCompanyByCode(notes);
        if (null == result) {
            EsCompanyVO esMemberMobileVO = new EsCompanyVO();
            return ApiResponse.success(esMemberMobileVO);
        }
        if (result.isSuccess()) {
            EsCompanyVO esMemberMobileVO = new EsCompanyVO();
            if (null != result.getData()) {
                BeanUtil.copyProperties(result.getData(), esMemberMobileVO);
            }
            return ApiResponse.success(esMemberMobileVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "编辑个人信息", notes = "编辑个人信息")
    @PutMapping
    @ResponseBody
    public ApiResponse editPersonInfo(@RequestBody @ApiParam(name = "personInfoForm",value = "用户对象",required = true) @Valid EsEditPersonInfoForm personInfoForm) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(personInfoForm, esMemberDTO);
        esMemberDTO.setId(userId);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
    @ApiOperation(value = "绑定手机号", notes = "绑定手机号")
    @PutMapping("/bind")
    @ResponseBody
    public ApiResponse bindMobile(@RequestBody @ApiParam(name = "personInfoForm",value = "用户对象",required = true) @Valid EsBindMobileForm personInfoForm) {
        //获取当前用户验证码
        String mobileCode = jedisCluster.get("mobile" + "_new_" + personInfoForm.getMobile());
        //比对
        if (StringUtils.isBlank(mobileCode) || !personInfoForm.getCode().equalsIgnoreCase(mobileCode)) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(personInfoForm, esMemberDTO);
        esMemberDTO.setId(userId);
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "找回密码", notes = "找回密码")
    @PutMapping("/newPassword")
    @ResponseBody
    public ApiResponse updatePassword(@RequestBody @ApiParam(name = "passWordForm",value = "密码对象",required = true) @Valid EsPassWordForm passWordForm) {
        DubboResult<EsMemberDO> result = iesMemberService.getMemberInfoByNameOrMobile(passWordForm.getMobile());
        if (!result.isSuccess() || result.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }
        EsMemberDO memberDO = result.getData();
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(memberDO.getId());
        //新密码
        String passWordNew = ShiroKit.md5(passWordForm.getNewPassword(), memberDO.getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        esMemberDTO.setSalt(salt);

        String newPassWord = ShiroKit.md5(passWordForm.getNewPassword(), memberDO.getName() + memberDO.getSalt());
        if(StringUtils.equals(memberDO.getPassword(), newPassWord)){
            return ApiResponse.fail(TradeErrorCode.PASSWORD_SAME.getErrorCode(), TradeErrorCode.PASSWORD_SAME.getErrorMsg());
        }

        //修改密码
        DubboResult memberResult = iesMemberService.updateMemberPass(esMemberDTO);
        if (memberResult.isSuccess()) {
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "验证手机号与图片验证码", notes = "验证手机号与图片验证码", response = EsCheckPasswordVO.class)
    @PutMapping("/checkPassword")
    @ResponseBody
    public ApiResponse checkPassword(@RequestBody MemberCheckForm form) {
        String validateCode = jedisCluster.get("findPassWordCode" + "_" + form.getUuid());
        //比对
        if (StringUtils.isBlank(form.getCaptcha()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(form.getCaptcha())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }
        DubboResult<EsMemberDO> result = iesMemberService.getMemberInfoByNameOrMobile(form.getNameOrMobile());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorMsg());
            }
            EsCheckPasswordVO checkPasswordVO = new EsCheckPasswordVO();
            checkPasswordVO.setMobile(result.getData().getMobile());
            checkPasswordVO.setUuid(form.getUuid());
            return ApiResponse.success(checkPasswordVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @GetMapping("/sendSMSCode")
    @ResponseBody
    public ApiResponse sendSMSCode(String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return ApiResponse.fail(MemberErrorCode.MOBILE_ERROR.getErrorCode(), MemberErrorCode.MOBILE_ERROR.getErrorMsg());
        }
        if(!FormatValidateUtils.isMobile(mobile)){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250663.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String mobielSign = "wap_member" + mobile;
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    /**
     * 验证验证码
     */
    @ApiOperation(value = "校验验证码", notes = "校验验证码")
    @PostMapping("/checkCode")
    @ResponseBody
    public ApiResponse checkCode(@RequestBody MemberCheckCodeForm checkCodeForm) {
        logger.info("校验验证码");
        if(StringUtils.isBlank(checkCodeForm.getCode()) || StringUtils.isBlank(checkCodeForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        String mobileCode = jedisCluster.get("wap_member" + checkCodeForm.getMobile());
        if(!mobileCode.equals(checkCodeForm.getCode())){
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
        }
        return ApiResponse.success();
    }
}

