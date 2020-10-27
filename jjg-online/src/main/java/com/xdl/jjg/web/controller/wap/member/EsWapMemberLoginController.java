package com.xdl.jjg.web.controller.wap.member;

import cn.hutool.core.util.RandomUtil;
import com.jjg.member.model.domain.EsMemberDO;
import com.jjg.member.model.domain.EsMemberTokenDO;
import com.jjg.member.model.dto.EsMemberDTO;
import com.jjg.member.model.dto.EsMemberTokenDTO;
import com.jjg.member.model.vo.EsLoginMemberVO;
import com.jjg.member.model.vo.EsMemberTokenVO;
import com.jjg.system.model.dto.EsSmsSendDTO;
import com.jjg.system.model.enums.SmsTemplateCodeEnum;
import com.jjg.trade.model.form.EsWapMemberLoginForm;
import com.jjg.trade.model.form.MemberFastLoginForm;
import com.xdl.jjg.constant.ApiStatus;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.FormatValidateUtils;
import com.xdl.jjg.util.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 移动端-用户登录模块 前端控制器
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-01-09 09:28:26
 */
@Api(value = "/wap/member/memberLogin", tags = "移动端-用户登录模块")
@RestController
@RequestMapping("/wap/member/memberLogin")
public class EsWapMemberLoginController {

    private static Logger logger = LoggerFactory.getLogger(EsWapMemberLoginController.class);

    @Autowired
    private JedisCluster jedisCluster;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberTokenService iEsMemberTokenService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsMemberService memberService;
    @Reference(version = "${dubbo.application.version}", timeout = 5000)
    private IEsSmsService esSmsService;


    //token过期时间(30天)
    @Value("${zhuox.shiro.expire}")
    private int EXPIRE;
    //验证码过期时间(5分钟)
    @Value("${zhuox.redisCode.expire}")
    private int CODEEXPIRE;
    //登录次数限制时间(24小时)
    @Value("${zhuox.loginLimit.expire}")
    private int LIMITEXPIRE;

    //加密长度
    @Value("${zhuo.member.encryption.length}")
    private int ENLENGTH;
    //短信验证码长度
    @Value("${zhuo.member.code.length}")
    private int SMSLENGTH;
    //短信失效时间
    @Value("${zhuox.shiro.member.sms.expire}")
    private int SMSEXPIRE;

    private String limitLogin= "SIGN_UP_LOGIN_COUNT_";



    /**
     * 账密登录
     */
    @ApiOperation(value = "账密登录")
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody EsWapMemberLoginForm form) {
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
                String loginTimesStr = jedisCluster.get(limitLogin + form.getNameOrMobile());
                Integer loginTimes = 1;
                if(loginTimesStr == null){
                    jedisCluster.set(limitLogin + form.getNameOrMobile(), String.valueOf(loginTimes));
                }else {
                    jedisCluster.incr(limitLogin + form.getNameOrMobile());
                }
                if(StringUtils.isNotBlank(loginTimesStr) && Integer.parseInt(loginTimesStr) >= 5){
                    jedisCluster.expire(limitLogin + form.getNameOrMobile(), LIMITEXPIRE);
                    return ApiResponse.fail(MemberErrorCode.LOGIN_PASS_ERROR_NUM.getErrorCode(), MemberErrorCode.LOGIN_PASS_ERROR_NUM.getErrorMsg());
                }
                return ApiResponse.fail(MemberErrorCode.MEMBER_PASS_ERROR.getErrorCode(), MemberErrorCode.MEMBER_PASS_ERROR.getErrorMsg());
            }

            EsLoginMemberVO es = new EsLoginMemberVO();
            es.setFace(userDo.getFace());
            es.setId(userDo.getId());
            es.setName(userDo.getName());
            es.setNickname(userDo.getNickname());
            //生成token，并保存到数据库
            ApiResponse token = createToken(userDo.getId());
            EsMemberTokenVO object = (EsMemberTokenVO) token.getData();
            es.setToken(object.getToken());
            EsMemberDTO memberDTOLogin = new EsMemberDTO();
            memberDTOLogin.setId(userDo.getId());
            memberService.updateMemberLastLoginTime(memberDTOLogin);

            //登录成功后删除之前错误登录次数
            jedisCluster.del(limitLogin + form.getNameOrMobile());
            return ApiResponse.success(es);
        }
        return ApiResponse.fail(MemberErrorCode.MEMBER_LAND_FAIL.getErrorCode(), MemberErrorCode.MEMBER_LAND_FAIL.getErrorMsg());
    }

    /**
     * 快捷登录
     */
    @ApiOperation(value = "手机号登录", notes = "手机号登录")
    @PostMapping("/fastLogin")
    @ResponseBody
    public ApiResponse fastLogin(@RequestBody MemberFastLoginForm form) {
        logger.info("POST请求登录");
        //判断手机号格式
        if(StringUtils.isBlank(form.getMobile()) || !FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        String mobielSign = "wap_member" + form.getMobile();
        String code = jedisCluster.get(mobielSign);
        if ( StringUtils.isBlank(code) || !form.getCode().equals(code)) {
            return ApiResponse.fail(MemberErrorCode.ERROR_SMS_CODE.getErrorCode(), MemberErrorCode.ERROR_SMS_CODE.getErrorMsg());
        }
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setNameOrMobile(form.getMobile());
        DubboResult<EsMemberDO> result = memberService.getMemberInfoByNameOrMobile(esMemberDTO.getNameOrMobile());
        if (result.isSuccess()) {
            EsMemberDO memberDO = result.getData();
            //注册
            if (memberDO == null) {
                esMemberDTO.setMobile(form.getMobile());
                DubboResult<EsMemberDO> memberDOResult = memberService.insertMember(esMemberDTO);
                if(!memberDOResult.isSuccess()){
                    return ApiResponse.fail(MemberErrorCode.MEMBER_LAND_FAIL.getErrorCode(), MemberErrorCode.MEMBER_LAND_FAIL.getErrorMsg());
                }
                memberDO = new EsMemberDO();
                memberDO.setId(memberDOResult.getData().getId());
            }
            // 清除验证码
              jedisCluster.del(mobielSign);
            //生成token，并保存到数据库
            return createToken(memberDO.getId());
        }
        return ApiResponse.fail(MemberErrorCode.MEMBER_LAND_FAIL.getErrorCode(), MemberErrorCode.MEMBER_LAND_FAIL.getErrorMsg());
    }


    /**
     * 退出
     */
    @ApiOperation(value = "退出", notes = "退出")
    @PostMapping("/logout")
    public ApiResponse logout() {
        //生成一个token
        JwtUtils jwtUtils = new JwtUtils();
        Map<String, Object> params = new HashMap<>();
        params.put("token", ShiroKit.getUser().getId());
        String token = jwtUtils.generateToken(params, "zhuoxdubbo", EXPIRE);
        //修改token
        EsMemberTokenDTO userTokenDTO = new EsMemberTokenDTO();
        userTokenDTO.setMemberId(ShiroKit.getUser().getId());
        userTokenDTO.setToken(token);
        iEsMemberTokenService.updateMemberToken(userTokenDTO);
        //清除缓存
        ShiroKit.getSubject().logout();
        return ApiResponse.success();
    }

    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @GetMapping("/sendSMSCode")
    @ResponseBody
    public ApiResponse sendSMSCode(@RequestParam(value ="mobile", required = true) String mobile) {
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
            String mobielSign = "wap_member" + mobile;
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }


    public ApiResponse createToken(Long userId) {
        //判断是否生成过token
        EsMemberTokenDTO userTokenDTO = new EsMemberTokenDTO();
        userTokenDTO.setMemberId(userId);
        DubboResult<EsMemberTokenDO> userToken = iEsMemberTokenService.getMemberTokenInfo(userTokenDTO);
        EsMemberTokenDO userTokenDO = userToken.getData();
        if (userToken.isSuccess()) {
            //当前时间
            Date now = new Date();
            //过期时间
//            Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
            Long time = now.getTime();
            Long expireTime = time + 2592000000l;
//            Long expireTime = now.getTime() + EXPIRE * 1000;
            //生成一个token
            Map<String, Object> params = new HashMap<>();
            params.put("token", userId);
            String token = JwtUtils.generateToken(params, "zhuoxdubbo", EXPIRE);
            if (null == userTokenDO) {
                userTokenDTO = new EsMemberTokenDTO();
                userTokenDTO.setMemberId(userId);
                userTokenDTO.setToken(token);
                userTokenDTO.setUpdateTime(now.getTime());
                userTokenDTO.setExpireTime(expireTime);
                //保存token
                iEsMemberTokenService.insertMemberToken(userTokenDTO);
                EsMemberTokenVO userTokenVo = new EsMemberTokenVO();
                userTokenVo.setToken(token);
                userTokenVo.setId(userId);
                userTokenVo.setUpdateTime(now.getTime());
                userTokenVo.setExpireTime(expireTime);
                return ApiResponse.success(userTokenVo);
            } else {
                if (userTokenDO.getExpireTime() < System.currentTimeMillis()) {
                    userTokenDTO.setToken(token);
                } else {
                    userTokenDTO.setToken(userTokenDO.getToken());
                }
                userTokenDTO.setUpdateTime(now.getTime());
                userTokenDTO.setExpireTime(expireTime);
                //更新token
                iEsMemberTokenService.updateMemberToken(userTokenDTO);
                EsMemberTokenVO userTokenVo = new EsMemberTokenVO();
                userTokenVo.setToken(userTokenDTO.getToken());
                userTokenVo.setId(userId);
                userTokenVo.setUpdateTime(now.getTime());
                userTokenVo.setExpireTime(expireTime);
                return ApiResponse.success(userTokenVo);
            }

        }
        return ApiResponse.fail(MemberErrorCode.LANDING_FAILURE.getErrorCode(), MemberErrorCode.LANDING_FAILURE.getErrorMsg());
    }
}