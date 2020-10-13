package com.xdl.jjg.web.controller.pc.member;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.web.BaseController;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberLevelConfigDO;
import com.shopx.member.api.model.domain.EsMyMeansDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.model.domain.vo.EsEditPersonInfoVO;
import com.shopx.member.api.model.domain.vo.EsMyMeansVO;
import com.shopx.member.api.service.*;
import com.shopx.system.api.model.domain.dto.EsSmsSendDTO;
import com.shopx.system.api.model.enums.SmsTemplateCodeEnum;
import com.shopx.system.api.service.IEsSmsService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsModifyMobileForm;
import com.shopx.trade.web.request.EsModifyPassWordForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.shopx.trade.web.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * <p>
 * 个人中心
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Api(value = "/personalSave", tags = "个人中心安全")
@RestController
@RequestMapping("/personalSave")
public class EsMemberPersonlSaveController extends BaseController {

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService iesMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsCompanyService iEsCompanyService;
    @Autowired
    private Producer producer;
    @Autowired
    private JedisCluster jedisCluster;
    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsSmsService esSmsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberLevelConfigService esMemberLevelConfigService;

    //短信/图片验证码失效时间为10分钟
    @Value("${zhuox.shiro.member.sms.expire}")
    private int SMSEXPIRE;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCouponService memberCouponService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCollectionGoodsService memberCollectionGoodsService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberCollectionShopService memberCollectionShopService;



    @ApiOperation(value = "重置密码", notes = "重新修改密码")
    @PutMapping("/oldPassword")
    @ResponseBody
    public ApiResponse updatePassword(@RequestBody @ApiParam(name = "esModifyPasswordForm",value = "修改密码对象",required = true) @Valid EsModifyPassWordForm esModifyPasswordForm) {
        if (StringUtils.isBlank(esModifyPasswordForm.getUuid())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_UUID_NOT_EMPTY.getErrorCode(), MemberErrorCode.MEMBER_UUID_NOT_EMPTY.getErrorMsg());
        }
        String validateCode = jedisCluster.get("passWordCode" + "_" + esModifyPasswordForm.getUuid());
        logger.info("session中的图形码字符串:" + validateCode);
        //比对
        if (StringUtils.isBlank(esModifyPasswordForm.getPasswordCode()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(esModifyPasswordForm.getPasswordCode())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }

        //获取当前用户
        ShiroUser user = ShiroKit.getUser();

        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberDO> memberInfo = iesMemberService.getMember(userId);
        if (!memberInfo.isSuccess()) {
            return ApiResponse.fail(memberInfo.getCode(), memberInfo.getMsg());
        }
        if (null == memberInfo.getData()) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }
        EsMemberDO memberDO = memberInfo.getData();

        //盐 加密
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        String passWord = ShiroKit.md5(esModifyPasswordForm.getOldPassword(), memberDO.getName() + memberDO.getSalt());
        if (!passWord.equals(memberDO.getPassword())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_PASS_ERROR.getErrorCode(), MemberErrorCode.MEMBER_PASS_ERROR.getErrorMsg());
        }

        //对比前后密码是否一样
        String newPassWord = ShiroKit.md5(esModifyPasswordForm.getNewPassword(), memberDO.getName() + memberDO.getSalt());
        if(StringUtils.equals(memberDO.getPassword(), newPassWord)){
            return ApiResponse.fail(TradeErrorCode.PASSWORD_SAME.getErrorCode(), TradeErrorCode.PASSWORD_SAME.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(userId);
        //新密码
        String passWordNew = ShiroKit.md5(esModifyPasswordForm.getNewPassword(), memberInfo.getData().getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        esMemberDTO.setSalt(salt);
        //修改密码
        DubboResult<EsMemberDO> result = iesMemberService.updateMemberPass(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del("passWordCode" + "_" + esModifyPasswordForm.getUuid());
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "重置密码-获取图片验证码", notes = "重置密码-获取图片验证码")
    @GetMapping("/passWordKaptcha.jpg")
    public void passWordKaptcha(HttpServletResponse response, String uuid) throws IOException {
        logger.info("前台请求的UUID:" + uuid);
        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();
        jedisCluster.setex("passWordCode" + "_" + uuid, SMSEXPIRE, code);
        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");
        BufferedImage image = producer.createImage(code);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        IOUtils.closeQuietly(outputStream);
    }

    @ApiOperation(value = "修改手机号-发送短信验证码无参", notes = "重置密码-发送短信验证码无参")
    @PostMapping("/sendSMSCodePassWord")
    @ResponseBody
    public ApiResponse sendSMSModifyMobile() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        DubboResult<EsMemberDO> memberInfo = iesMemberService.getMember(userId);
        esSmsSendDTO.setMobile(memberInfo.getData().getMobile());
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250667.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String mobielSign = "mobile" + "_old_" + memberInfo.getData().getMobile();
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改手机号-判断短信验证码是否准确", notes = "重置密码-判断短信验证码是否准确")
    @PostMapping("/judeSmsCode/{smsCode}")
    @ResponseBody
    public ApiResponse judeSmsCode(String smsCode) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberDO> memberInfo = iesMemberService.getMember(userId);
        if (!memberInfo.isSuccess()) {
            return ApiResponse.fail(memberInfo.getCode(), memberInfo.getMsg());
        }
        if (null == memberInfo.getData()) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }
        String mobileCode = jedisCluster.get("mobile" + "_old_" + memberInfo.getData().getMobile());
        logger.info("session中短信码字符串:" + mobileCode);
        Boolean bool = mobileCode.equals(smsCode);
        if (!bool) {
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
        }
        return ApiResponse.success(mobileCode);

    }

    @ApiOperation(value = "修改手机号", notes = "修改手机号")
    @PutMapping("/oldMobile")
    @ResponseBody
    public ApiResponse updateMobile(@RequestBody @ApiParam(name = "esModifyMobileForm",value = "修改密码对象",required = true) @Valid EsModifyMobileForm esModifyMobileForm) {
        //获取当前用户
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        if (StringUtils.isBlank(esModifyMobileForm.getCode())) {
            return ApiResponse.fail(MemberErrorCode.ERROR_SMS_CODE.getErrorCode(), MemberErrorCode.ERROR_SMS_CODE.getErrorMsg());
        }
        if (StringUtils.isBlank(esModifyMobileForm.getMobile())) {
            return ApiResponse.fail(MemberErrorCode.MOBILE_ERROR.getErrorCode(), MemberErrorCode.MOBILE_ERROR.getErrorMsg());
        }
        DubboResult<EsMemberDO> memberInfo = iesMemberService.getMember(userId);
        if (!memberInfo.isSuccess()) {
            return ApiResponse.fail(memberInfo.getCode(), memberInfo.getMsg());
        }
        if (null == memberInfo.getData()) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }
        EsMemberDO memberDO = memberInfo.getData();
        DubboResult<EsMemberDO> resultMobile = iesMemberService.getMemberInfoByNameOrMobile(esModifyMobileForm.getMobile());
        if (resultMobile.isSuccess()) {
            if (resultMobile.getData() != null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorMsg());
            }
        }
        if(esModifyMobileForm.getMobile().equals(memberDO.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_ALREADY_BINDING.getErrorCode(), MemberErrorCode.MOBILE_ALREADY_BINDING.getErrorMsg());
        }
        //获取当前用户验证码
        String mobileCode = jedisCluster.get("mobile" + "_new_" + esModifyMobileForm.getMobile());
        //比对
        if (StringUtils.isBlank(mobileCode) || !esModifyMobileForm.getCode().equalsIgnoreCase(mobileCode)) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(userId);
        esMemberDTO.setMobile(esModifyMobileForm.getMobile());
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del("mobile" + "_new_" + esModifyMobileForm.getMobile());
            return ApiResponse.success(result.getData());
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改手机号-发送的验证码，就是手机号和验证码同时存在的", notes = "修改手机号-发送的验证码")
    @PostMapping("/sendMobileCode/{mobile}")
    @ResponseBody
    public ApiResponse sendMobileSmsCode(String mobile) {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberDO> memberInfo = iesMemberService.getMember(userId);
        if (!memberInfo.isSuccess()) {
            return ApiResponse.fail(memberInfo.getCode(), memberInfo.getMsg());
        }

        if (null == memberInfo.getData()) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }
        if (null == memberInfo.getData().getMobile()) {
            return ApiResponse.fail(MemberErrorCode.ITEM_NOT_FOUND.getErrorCode(), MemberErrorCode.ITEM_NOT_FOUND.getErrorMsg());
        }

        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId("SMS_105250667");
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String mobileCodeSign = "mobile" + "_new_" + mobile;
            jedisCluster.setex(mobileCodeSign, SMSEXPIRE, code);
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));

    }

    @ApiOperation(value = "查询会员个人信息", notes = "查询会员个人信息", response = EsEditPersonInfoVO.class)
    @GetMapping("/getPersonInfo")
    @ResponseBody
    public ApiResponse getPersonInfo() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        DubboResult<EsMemberDO> result = iesMemberService.getAdminMember(userId);
        if (result.isSuccess() && null != result.getData()) {
            EsEditPersonInfoVO esEditPersonInfoVO = new EsEditPersonInfoVO();
            BeanUtil.copyProperties(result.getData(), esEditPersonInfoVO);
            if( null != result.getData().getGrade() && result.getData().getGrade() != 0){
                DubboResult<EsMemberLevelConfigDO> resultGrade = esMemberLevelConfigService.getMemberLevelByGrade(result.getData().getGrade());
                if(resultGrade.isSuccess() &&  null != resultGrade.getData().getLevel()){
                    esEditPersonInfoVO.setGradeLevel(resultGrade.getData().getLevel());
                }
            }
            return ApiResponse.success(DubboPageResult.success(esEditPersonInfoVO));
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "我的资产统计", notes = "我的资产统计", response = EsMyMeansVO.class)
    @GetMapping("/myMeans")
    @ResponseBody
    public ApiResponse myMeans() {
        ShiroUser user = ShiroKit.getUser();
        if (null == user) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        Long userId = user.getId();
        EsMyMeansVO myMeansVO = new EsMyMeansVO();
        DubboResult<EsMyMeansDO> result = iesMemberService.meansCensus(userId);
        if (result.isSuccess() && null != result.getData()) {
            BeanUtil.copyProperties(result.getData(), myMeansVO);
        }
        return ApiResponse.success(myMeansVO);
    }
}

