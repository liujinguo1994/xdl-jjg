package com.xdl.jjg.web.controller.applet.member;

import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shopx.common.exception.ArgumentException;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboPageResult;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.common.util.security.BouncyCastleProvider;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsOpenidMobileDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.model.domain.dto.EsOpenidMobileDTO;
import com.shopx.member.api.model.domain.vo.EsAppletUserInfoVO;
import com.shopx.member.api.model.domain.vo.EsOpenidMobileVO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsOpenidMobileService;
import com.shopx.system.api.model.domain.dto.EsSmsSendDTO;
import com.shopx.system.api.model.enums.SmsTemplateCodeEnum;
import com.shopx.system.api.service.IEsSmsService;
import com.shopx.trade.api.constant.TradeErrorCode;
import com.shopx.trade.api.utils.HttpClientUtil;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.*;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.spec.InvalidParameterSpecException;
import java.util.*;

/**
 * <p>
 * 小程序-用户登录模块
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-04-27 09:28:26
 */
@Api(value = "/applet/member/login", tags = "小程序-用户登录模块")
@RequestMapping("/applet/member/login")
@RestController
public class EsAppletLoginController {

    private static Logger logger = LoggerFactory.getLogger(EsAppletLoginController.class);

    @Autowired
    private JedisCluster jedisCluster;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSmsService esSmsService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsOpenidMobileService openidMobileService;

    //短信验证码长度
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    //短信验证码失效时间
    @Value("${zhuox.shiro.member.sms.expire}")
    private int SMSEXPIRE;
    private String limitLogin= "SIGN_UP_LOGIN_COUNT_";
    //登录次数限制时间(24小时)
    @Value("${zhuox.loginLimit.expire}")
    private int LIMITEXPIRE;


    @ApiOperation(value = "手机号快捷登录获取短信验证码")
    @GetMapping("/fastLoginSendSMS/{mobile}")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse fastLoginSendSMS(@PathVariable String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return ApiResponse.fail(MemberErrorCode.MOBILE_ERROR.getErrorCode(), MemberErrorCode.MOBILE_ERROR.getErrorMsg());
        }
        if(!FormatValidateUtils.isMobile(mobile)){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250667.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String key = "applet_fast_login_" + mobile;
            jedisCluster.setex(key, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    @ApiOperation(value = "微信登录使用其他手机号登录获取短信验证码")
    @GetMapping("/wxLoginSendSMS/{mobile}")
    @ApiImplicitParam(name = "mobile", value = "手机号", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse wxLoginSendSMS(@PathVariable String mobile) {
        if (StringUtils.isBlank(mobile)) {
            return ApiResponse.fail(MemberErrorCode.MOBILE_ERROR.getErrorCode(), MemberErrorCode.MOBILE_ERROR.getErrorMsg());
        }
        if(!FormatValidateUtils.isMobile(mobile)){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        EsSmsSendDTO esSmsSendDTO = new EsSmsSendDTO();
        esSmsSendDTO.setMobile(mobile);
        String code = RandomUtil.randomNumbers(SMSLENGTH);
        esSmsSendDTO.setTemplateId(SmsTemplateCodeEnum.SMS_105250667.value());
        esSmsSendDTO.setCode(code);
        DubboResult result = esSmsService.send(esSmsSendDTO);
        if (result.isSuccess()) {
            String key = "applet_wx_login_" + mobile;
            jedisCluster.setex(key, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 快捷登录
     */
    @ApiOperation(value = "手机号快捷登录")
    @PostMapping("/fastLogin")
    @ResponseBody
    public ApiResponse fastLogin(@RequestBody @Valid EsAppletFastLoginForm form) {
        logger.info("手机号快捷登录的参数:"+ JSON.toJSONString(form));

        //判断手机号格式
        if(!FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        //判断验证码
        String key = "applet_fast_login_" + form.getMobile();
        String verificationCode = jedisCluster.get(key);
        if ( StringUtils.isBlank(verificationCode) || !form.getVerificationCode().equals(verificationCode)) {
            return ApiResponse.fail(MemberErrorCode.ERROR_SMS_CODE.getErrorCode(), MemberErrorCode.ERROR_SMS_CODE.getErrorMsg());
        }
        //获取openid和sessionKey
        JSONObject sessionKeyOpenId = null;
        try {
            sessionKeyOpenId = getSessionKeyOrOpenId(form.getCode());
            logger.info("post请求获取的sessionKeyOpenId=" + sessionKeyOpenId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(1001,"获取openid异常!");
        }
        String openid = sessionKeyOpenId.getString("openid");

        DubboResult<EsMemberDO> result = memberService.getMemberInfoByMobile(form.getMobile());
        if (result.isSuccess()){
            //uuid生成唯一登录态标识
            String skey = UUID.randomUUID().toString();
            EsMemberDO memberDO = result.getData();
            if (memberDO == null){
                //账号不存在注册用户信息
                EsMemberDTO memberDTO = new EsMemberDTO();
                memberDTO.setMobile(form.getMobile());
                memberDTO.setOpenid(openid);
                memberDTO.setSkey(skey);
                DubboResult<EsMemberDO> memberDOResult = memberService.insertMember(memberDTO);
                if(!memberDOResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(memberDOResult));
                }
            }else {
                //账号存在更新用户信息
                //判断该账号是否已在其他微信登录
                if (StringUtils.isNotBlank(memberDO.getOpenid()) && !memberDO.getOpenid().equals(openid)){
                    return ApiResponse.fail(1002,"该账号已在其他微信登录!");
                }
                //更新
                EsMemberDTO dto = new EsMemberDTO();
                BeanUtil.copyProperties(memberDO,dto);
                dto.setOpenid(openid);
                dto.setSkey(skey);
                DubboResult<EsMemberDO> dubboResult = memberService.updateMember(dto);
                if(!dubboResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
                }
            }
            // 清除验证码
            jedisCluster.del(key);
            return ApiResponse.success(skey);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 账密登录
     */
    @ApiOperation(value = "账密登录")
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody @Valid EsAppletLoginForm form) {
        logger.info("账密登录的参数:"+ JSON.toJSONString(form));
        //根据用户名或者手机号获取用户信息
        DubboResult<EsMemberDO> result = memberService.getMemberInfoByNameOrMobile(form.getNameOrMobile());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorMsg());
            }
            EsMemberDO userDo = result.getData();
            if(userDo.getState() == 1){
                return ApiResponse.fail(MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorCode(), MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorMsg());
            }
            //判断密码是否正确
            if (!userDo.getPassword().equals(ShiroKit.md5(form.getPassword(), userDo.getName() + userDo.getSalt()))) {
                //密码错误限制登录次数
                String count = jedisCluster.get(limitLogin + form.getNameOrMobile());
                if(StringUtils.isNotBlank(count)){
                    jedisCluster.incr(limitLogin + form.getNameOrMobile());
                }else {
                    if(Integer.parseInt(count) > 5){
                        jedisCluster.expire(limitLogin + form.getNameOrMobile(), LIMITEXPIRE);
                        return ApiResponse.fail(MemberErrorCode.LOGIN_PASS_ERROR_NUM.getErrorCode(), MemberErrorCode.LOGIN_PASS_ERROR_NUM.getErrorMsg());
                    }
                    jedisCluster.incr(limitLogin + form.getNameOrMobile());
                }
                return ApiResponse.fail(MemberErrorCode.MEMBER_PASS_ERROR.getErrorCode(), MemberErrorCode.MEMBER_PASS_ERROR.getErrorMsg());
            }

            //获取openid和sessionKey
            JSONObject sessionKeyOpenId = null;
            try {
                sessionKeyOpenId = getSessionKeyOrOpenId(form.getCode());
                logger.info("post请求获取的sessionKeyOpenId=" + sessionKeyOpenId);
            } catch (Exception e) {
                e.printStackTrace();
                return ApiResponse.fail(1001,"获取openid异常!");
            }
            String openid = sessionKeyOpenId.getString("openid");
            //uuid生成唯一登录态标识
            String skey = UUID.randomUUID().toString();

            //判断该账号是否已在其他微信登录
            if (StringUtils.isNotBlank(userDo.getOpenid()) && !userDo.getOpenid().equals(openid)){
                return ApiResponse.fail(1002,"该账号已在其他微信登录!");
            }
            //更新用户信息
            EsMemberDTO dto = new EsMemberDTO();
            BeanUtil.copyProperties(userDo,dto);
            dto.setOpenid(openid);
            dto.setSkey(skey);
            DubboResult<EsMemberDO> dubboResult = memberService.updateMember(dto);
            if(!dubboResult.isSuccess()){
                return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
            }

            //登录成功后删除之前错误登录次数
            jedisCluster.del(limitLogin + form.getNameOrMobile());

            return ApiResponse.success(skey);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 微信登录-使用微信绑定的手机号登录
     */
    @ApiOperation(value = "微信登录-使用微信绑定的手机号登录")
    @PostMapping("/wxMobileLogin")
    @ResponseBody
    public ApiResponse wxMobileLogin(@RequestBody @Valid EsWxMobileLoginForm form) {
        logger.info("微信登录-使用微信绑定的手机号登录参数:"+ JSON.toJSONString(form));

        //判断手机号格式
        if(!FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }

        //获取openid和sessionKey
        JSONObject sessionKeyOpenId = null;
        try {
            sessionKeyOpenId = getSessionKeyOrOpenId(form.getCode());
            logger.info("post请求获取的sessionKeyOpenId=" + sessionKeyOpenId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(1001,"获取openid异常!");
        }
        String openid = sessionKeyOpenId.getString("openid");

        DubboResult<EsMemberDO> result = memberService.getMemberInfoByMobile(form.getMobile());
        if (result.isSuccess()){
            //uuid生成唯一登录态标识
            String skey = UUID.randomUUID().toString();
            EsMemberDO memberDO = result.getData();
            if (memberDO == null){
                //账号不存在注册用户信息
                EsMemberDTO memberDTO = new EsMemberDTO();
                memberDTO.setMobile(form.getMobile());
                memberDTO.setOpenid(openid);
                memberDTO.setSkey(skey);
                DubboResult<EsMemberDO> memberDOResult = memberService.insertMember(memberDTO);
                if(!memberDOResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(memberDOResult));
                }
            }else {
                //账号存在更新用户信息
                //判断该账号是否已在其他微信登录
                if (StringUtils.isNotBlank(memberDO.getOpenid()) && !memberDO.getOpenid().equals(openid)){
                    return ApiResponse.fail(1002,"该账号已在其他微信登录!");
                }
                //更新
                EsMemberDTO dto = new EsMemberDTO();
                BeanUtil.copyProperties(memberDO,dto);
                dto.setOpenid(openid);
                dto.setSkey(skey);
                DubboResult<EsMemberDO> dubboResult = memberService.updateMember(dto);
                if(!dubboResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
                }
            }
            return ApiResponse.success(skey);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 微信登录-使用其他手机号登录
     */
    @ApiOperation(value = "微信登录-使用其他手机号登录")
    @PostMapping("/otherMobileLogin")
    @ResponseBody
    public ApiResponse otherMobileLogin(@RequestBody @Valid EsotherMobileLoginForm form) {
        logger.info("微信登录-使用其他手机号登录参数:"+ JSON.toJSONString(form));

        //判断手机号格式
        if(!FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        //判断验证码
        String key = "applet_wx_login_" + form.getMobile();
        String verificationCode = jedisCluster.get(key);
        if ( StringUtils.isBlank(verificationCode) || !form.getVerificationCode().equals(verificationCode)) {
            return ApiResponse.fail(MemberErrorCode.ERROR_SMS_CODE.getErrorCode(), MemberErrorCode.ERROR_SMS_CODE.getErrorMsg());
        }
        //获取openid和sessionKey
        JSONObject sessionKeyOpenId = null;
        try {
            sessionKeyOpenId = getSessionKeyOrOpenId(form.getCode());
            logger.info("post请求获取的sessionKeyOpenId=" + sessionKeyOpenId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(1001,"获取openid异常!");
        }
        String openid = sessionKeyOpenId.getString("openid");

        DubboResult<EsMemberDO> result = memberService.getMemberInfoByMobile(form.getMobile());
        if (result.isSuccess()){
            //uuid生成唯一登录态标识
            String skey = UUID.randomUUID().toString();
            EsMemberDO memberDO = result.getData();
            if (memberDO == null){
                //账号不存在注册用户信息
                EsMemberDTO memberDTO = new EsMemberDTO();
                memberDTO.setMobile(form.getMobile());
                memberDTO.setOpenid(openid);
                memberDTO.setSkey(skey);
                DubboResult<EsMemberDO> memberDOResult = memberService.insertMember(memberDTO);
                if(!memberDOResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(memberDOResult));
                }
            }else {
                //账号存在更新用户信息
                //判断该账号是否已在其他微信登录
                if (StringUtils.isNotBlank(memberDO.getOpenid()) && !memberDO.getOpenid().equals(openid)){
                    return ApiResponse.fail(1002,"该账号已在其他微信登录!");
                }
                //更新
                EsMemberDTO dto = new EsMemberDTO();
                BeanUtil.copyProperties(memberDO,dto);
                dto.setOpenid(openid);
                dto.setSkey(skey);
                DubboResult<EsMemberDO> dubboResult = memberService.updateMember(dto);
                if(!dubboResult.isSuccess()){
                    return ApiResponse.fail(ApiStatus.wrapperException(dubboResult));
                }
            }
            if(form.getSave() == 2){
                //插入微信关联此号码的数据(保存此号码供以后授权使用)
                EsOpenidMobileDTO dto = new EsOpenidMobileDTO();
                dto.setOpenid(openid);
                dto.setMobile(form.getMobile());
                openidMobileService.insertOpenidMobile(dto);
            }
            // 清除验证码
            jedisCluster.del(key);
            return ApiResponse.success(skey);
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }


    /**
     * 退出登录
     */
    @ApiOperation(value = "退出登录")
    @PutMapping("/quitLogin/{skey}")
    @ApiImplicitParam(name = "skey", value = "登录态标识", required = true, dataType = "String", paramType = "path")
    @ResponseBody
    public ApiResponse quitLogin(@PathVariable String skey) {
        if (StringUtils.isBlank(skey)){
            throw new ArgumentException(TradeErrorCode.PARAM_ERROR.getErrorCode(), "参数错误");
        }
        DubboResult result = memberService.quitLogin(skey);
        if (result.isSuccess()){
            return ApiResponse.success();
        }else {
            return ApiResponse.fail(ApiStatus.wrapperException(result));
        }
    }

    /**
     * 解密用户敏感信息及获取此微信关联的其他手机号码
     */
    @ApiOperation(value = "解密用户敏感信息及获取此微信关联的其他手机号码")
    @PostMapping("/decodeUser")
    @ResponseBody
    public ApiResponse decodeUser(@RequestBody @Valid EsDecodeUserForm form) {
        logger.info("解密用户敏感信息的参数:"+ JSON.toJSONString(form));

        //获取openid和sessionKey
        JSONObject sessionKeyOpenId = null;
        try {
            sessionKeyOpenId = getSessionKeyOrOpenId(form.getCode());
            logger.info("post请求获取的sessionKeyOpenId=" + sessionKeyOpenId);
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.fail(1001,"获取openid异常!");
        }
        String openid = sessionKeyOpenId.getString("openid");
        String sessionKey = sessionKeyOpenId.getString("session_key");

        //解密用户敏感数据获取用户信息
        JSONObject jsonObject = getUserInfo(form.getEncrypteData(), sessionKey, form.getIv());
        if (jsonObject == null){
            return ApiResponse.fail(1003,"解密用户敏感数据异常!");
        }
        logger.info("根据解密算法获取的userInfo=" + jsonObject);
        String phoneNumber = jsonObject.getString("phoneNumber");
        EsAppletUserInfoVO userInfoVO = new EsAppletUserInfoVO();
        userInfoVO.setPhoneNumber(phoneNumber);
        DubboPageResult<EsOpenidMobileDO> result = openidMobileService.getListByOpenid(openid);
        if (result.isSuccess() && CollectionUtils.isNotEmpty(result.getData().getList())){
            List<EsOpenidMobileDO> data = result.getData().getList();
            List<EsOpenidMobileVO> voList = BeanUtil.copyList(data, EsOpenidMobileVO.class);
            userInfoVO.setMobileVOList(voList);
        }
        return ApiResponse.success(userInfoVO);
    }



    //获取openid和sessionKey方法
    public static JSONObject getSessionKeyOrOpenId(String code) throws Exception {
        //微信端登录code
        String wxCode = code;
        String requestUrl = "https://api.weixin.qq.com/sns/jscode2session";
        Map<String,Object> requestUrlParam = new HashMap<String, Object>(  );
        requestUrlParam.put( "appid","wx0aff75480f2519ea" );//小程序appId
        requestUrlParam.put( "secret","ee74bc810e6b138c67b23d64644d7760" );
        requestUrlParam.put( "js_code",wxCode );//小程序端返回的code
        requestUrlParam.put( "grant_type","authorization_code" );//默认参数

        //发送post请求读取调用微信接口获取openid用户唯一标识
        HttpClientUtil httpClientUtil = HttpClientUtil.getInstance();
        String s = httpClientUtil.sendHttpPost(requestUrl, requestUrlParam);
        JSONObject jsonObject = JSON.parseObject(s);
        return jsonObject;
    }

    //解密用户敏感数据获取用户信息
    public static JSONObject getUserInfo(String encryptedData, String sessionKey, String iv){
        // 被加密的数据
        byte[] dataByte = Base64.decode(encryptedData);
        // 加密秘钥
        byte[] keyByte = Base64.decode(sessionKey);
        // 偏移量
        byte[] ivByte = Base64.decode(iv);
        try {
            // 如果密钥不足16位，那么就补足.  这个if 中的内容很重要
            int base = 16;
            if (keyByte.length % base != 0) {
                int groups = keyByte.length / base + (keyByte.length % base != 0 ? 1 : 0);
                byte[] temp = new byte[groups * base];
                Arrays.fill(temp, (byte) 0);
                System.arraycopy(keyByte, 0, temp, 0, keyByte.length);
                keyByte = temp;
            }
            // 初始化
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding","BC");
            SecretKeySpec spec = new SecretKeySpec(keyByte, "AES");
            AlgorithmParameters parameters = AlgorithmParameters.getInstance("AES");
            parameters.init(new IvParameterSpec(ivByte));
            cipher.init( Cipher.DECRYPT_MODE, spec, parameters);// 初始化
            byte[] resultByte = cipher.doFinal(dataByte);
            if (null != resultByte && resultByte.length > 0) {
                String result = new String(resultByte, "UTF-8");
                return JSON.parseObject(result);
            }
        } catch (NoSuchAlgorithmException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidParameterSpecException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalBlockSizeException e) {
            logger.error(e.getMessage(), e);
        } catch (BadPaddingException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            logger.error(e.getMessage(), e);
        } catch (InvalidAlgorithmParameterException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchProviderException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}