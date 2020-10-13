package com.xdl.jjg.web.controller.applet.member;

import cn.hutool.core.util.RandomUtil;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.system.api.model.domain.dto.EsSmsSendDTO;
import com.shopx.system.api.model.enums.SmsTemplateCodeEnum;
import com.shopx.system.api.service.IEsSmsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.*;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.validation.Valid;

/**
 * <p>
 * 小程序-设置 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-20 13:28:26
 */
@Api(value = "/applet/member/memberSet", tags = "小程序-设置")
@RestController
@RequestMapping("/applet/member/memberSet")
public class EsAppletMemberSetController {

    private static Logger logger = LoggerFactory.getLogger(EsAppletMemberSetController.class);

    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService iesMemberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSmsService esSmsService;

    @Autowired
    private JedisCluster jedisCluster;
    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    //短信/图片验证码失效时间为10分钟
    @Value("${zhuox.shiro.member.sms.expire}")
    private int SMSEXPIRE;

    @ApiOperation(value = "设置登录密码")
    @PutMapping("/setPassword")
    @ResponseBody
    public ApiResponse setPassword(@RequestBody @Valid EsAppletSetPasswordForm form) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = iesMemberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(memberDO.getId());
        //盐
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        esMemberDTO.setSalt(salt);
        //密码加密
        String passWordNew = ShiroKit.md5(form.getPassword(), memberDO.getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        //修改密码
        DubboResult result = iesMemberService.updateMemberPass(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "重置密码")
    @PutMapping("/updatePassword")
    @ResponseBody
    public ApiResponse updatePassword(@RequestBody @Valid EsAppletModifyPasswordForm form) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = iesMemberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
       //校验旧密码
        String passWord = ShiroKit.md5(form.getOldPassword(), memberDO.getName() + memberDO.getSalt());
        if (!passWord.equals(memberDO.getPassword())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_PASS_ERROR.getErrorCode(), MemberErrorCode.MEMBER_PASS_ERROR.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(memberDO.getId());
        //盐 加密
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        //新密码
        String passWordNew = ShiroKit.md5(form.getNewPassword(), memberDO.getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        esMemberDTO.setSalt(salt);
        //修改密码
        DubboResult result = iesMemberService.updateMemberPass(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改手机号-发送短信验证码(旧手机号)")
    @PostMapping("/oldMobileSendSmsCode/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse oldMobileSendSmsCode(@PathVariable String skey) {
        DubboResult<EsMemberDO> dubboResult = iesMemberService.getMemberBySkey(skey);
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(memberDO.getMobile());
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250667.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String mobielSign = "applet_update_mobile_old" + memberDO.getMobile();
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改手机号-判断短信验证码是否准确(旧手机号)")
    @PostMapping("/checkSmsCode")
    @ResponseBody
    public ApiResponse checkSmsCode(@RequestBody @Valid EsAppletCheckSmsCodeForm form) {
        DubboResult<EsMemberDO> dubboResult = iesMemberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();

        String code = jedisCluster.get("applet_update_mobile_old" + memberDO.getMobile());
        boolean b = code != null && code.equals(form.getSmsCode());
        if (!b) {
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
        }
        //清除验证码
        jedisCluster.del("applet_update_mobile_old" + memberDO.getMobile());
        return ApiResponse.success();
    }

    @ApiOperation(value = "修改手机号-发送短信验证码(新手机号)")
    @PostMapping("/sendSMSCode/{mobile}")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse sendSMSCode(@PathVariable String mobile) {
        if(StringUtils.isBlank(mobile) || !FormatValidateUtils.isMobile(mobile)){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250667.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String mobielSign = "applet_update_mobile" +mobile;
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "修改手机号")
    @PutMapping("/updateMobile")
    @ResponseBody
    public ApiResponse updateMobile(@RequestBody @Valid EsAppletModifyMobileForm form) {
        //获取当前用户
        DubboResult<EsMemberDO> dubboResult = iesMemberService.getMemberBySkey(form.getSkey());
        if (!dubboResult.isSuccess() || dubboResult.getData() == null) {
            return ApiResponse.fail(MemberErrorCode.NOT_LOGIN.getErrorCode(), MemberErrorCode.NOT_LOGIN.getErrorMsg());
        }
        if(!FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        EsMemberDO memberDO = dubboResult.getData();

        DubboResult<EsMemberDO> resultMobile = iesMemberService.getMemberInfoByNameOrMobile(form.getMobile());
        if (resultMobile.isSuccess() && resultMobile.getData() != null) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorMsg());
        }

        //获取当前用户验证码
        String mobileCode = jedisCluster.get("applet_update_mobile" + form.getMobile());
        //比对
        if (StringUtils.isBlank(mobileCode) || !form.getCode().equalsIgnoreCase(mobileCode)) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(memberDO.getId());
        esMemberDTO.setMobile(form.getMobile());
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del("applet_update_mobile" + form.getMobile());
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

