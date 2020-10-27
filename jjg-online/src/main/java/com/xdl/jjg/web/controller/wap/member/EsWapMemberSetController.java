package com.xdl.jjg.web.controller.wap.member;

import cn.hutool.core.util.RandomUtil;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.system.model.dto.EsSmsSendDTO;
import com.jjg.system.model.enums.SmsTemplateCodeEnum;
import com.jjg.trade.model.form.EsWapModifyPassWordForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.ApiResponse;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
 * 移动端-我的-设置 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/member/memberSet", tags = "移动端-我的-设置")
@RestController
@RequestMapping("/wap/member/memberSet")
public class EsWapMemberSetController {

    private static Logger logger = LoggerFactory.getLogger(EsWapMemberSetController.class);

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



    @ApiOperation(value = "重置密码", notes = "重新修改密码")
    @PutMapping("/updatePassword")
    @ResponseBody
    public ApiResponse updatePassword(@RequestBody @ApiParam(name = "form",value = "修改密码对象",required = true) @Valid EsWapModifyPassWordForm form) {
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

       //校验旧密码
        String passWord = ShiroKit.md5(form.getOldPassword(), memberInfo.getData().getName() + memberInfo.getData().getSalt());
        if (!passWord.equals(memberInfo.getData().getPassword())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_PASS_ERROR.getErrorCode(), MemberErrorCode.MEMBER_PASS_ERROR.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(userId);
        //盐 加密
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        //新密码
        String passWordNew = ShiroKit.md5(form.getNewPassword(), memberInfo.getData().getName() + salt);
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
            String mobielSign = "wap_update_mobile_old" + memberInfo.getData().getMobile();
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改手机号-判断短信验证码是否准确(旧手机号)")
    @PostMapping("/judeSmsCode/{smsCode}")
    @ApiImplicitParam(name = "smsCode", value = "验证码", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse judeSmsCode(@PathVariable String smsCode) {
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
        String mobileCode = jedisCluster.get("wap_update_mobile_old" + memberInfo.getData().getMobile());
        logger.info("session中短信码字符串:" + mobileCode);
        boolean bool = mobileCode != null && mobileCode.equals(smsCode);
        if (!bool) {
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
        }
        //清除验证码
        jedisCluster.del("wap_update_mobile_old" + memberInfo.getData().getMobile());
        return ApiResponse.success();

    }

    @ApiOperation(value = "发送短信验证码")
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
            String mobielSign = "wap_update_mobile" +mobile;
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }

    @ApiOperation(value = "修改手机号", notes = "修改手机号")
    @PutMapping("/oldMobile")
    @ResponseBody
    public ApiResponse updateMobile(@RequestBody @ApiParam(name = "esModifyMobileForm",value = "修改手机号对象",required = true) @Valid EsModifyMobileForm esModifyMobileForm) {
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
        if(!FormatValidateUtils.isMobile(esModifyMobileForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
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
        if (resultMobile.isSuccess() && resultMobile.getData() != null) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_IS_EXIST.getErrorMsg());
        }
        if(esModifyMobileForm.getMobile().equals(memberDO.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_ALREADY_BINDING.getErrorCode(), MemberErrorCode.MOBILE_ALREADY_BINDING.getErrorMsg());
        }
        //获取当前用户验证码
        String mobileCode = jedisCluster.get("wap_update_mobile" + esModifyMobileForm.getMobile());
        //比对
        if (StringUtils.isBlank(mobileCode) || !esModifyMobileForm.getCode().equalsIgnoreCase(mobileCode)) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(userId);
        esMemberDTO.setMobile(esModifyMobileForm.getMobile());
        DubboResult result = iesMemberService.updateMemberInfo(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del("wap_update_mobile" + esModifyMobileForm.getMobile());
            return ApiResponse.success();
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    @ApiOperation(value = "设置登录密码")
    @PutMapping("/setPassword/{password}")
    @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse setPassword(@PathVariable String password) {
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

        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setId(userId);
        //盐
        String salt = ShiroKit.getRandomSalt(ENLENGTH);
        esMemberDTO.setSalt(salt);
        //密码加密
        String passWordNew = ShiroKit.md5(password, memberInfo.getData().getName() + salt);
        esMemberDTO.setPassword(passWordNew);
        //修改密码
        DubboResult result = iesMemberService.updateMemberPass(esMemberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "个人资料设置")
    @PutMapping("/setMemberInfo")
    @ResponseBody
    public ApiResponse setMemberInfo(@RequestBody EsWapSetMemberInfoForm form) {
        EsMemberDTO memberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(form,memberDTO);
        DubboResult result = iesMemberService.updateMemberInfo(memberDTO);
        if (result.isSuccess()) {
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }
}

