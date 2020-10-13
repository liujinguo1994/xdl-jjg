package com.xdl.jjg.web.controller.pc.member;

import cn.hutool.core.util.RandomUtil;
import com.google.code.kaptcha.Producer;
import com.shopx.common.model.result.ApiResponse;
import com.shopx.common.model.result.DubboResult;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.FormatValidateUtils;
import com.shopx.common.util.IPUtil;
import com.shopx.common.util.JwtUtils;
import com.shopx.member.api.constant.MemberErrorCode;
import com.shopx.member.api.model.domain.EsMemberDO;
import com.shopx.member.api.model.domain.EsMemberTokenDO;
import com.shopx.member.api.model.domain.dto.EsMemberDTO;
import com.shopx.member.api.model.domain.dto.EsMemberTokenDTO;
import com.shopx.member.api.model.domain.vo.EsLoginMemberVO;
import com.shopx.member.api.model.domain.vo.EsMemberTokenInfoVO;
import com.shopx.member.api.model.domain.vo.EsMemberTokenVO;
import com.shopx.member.api.service.IEsMemberService;
import com.shopx.member.api.service.IEsMemberTokenService;
import com.shopx.system.api.model.domain.dto.EsSmsSendDTO;
import com.shopx.system.api.model.enums.SmsTemplateCodeEnum;
import com.shopx.system.api.service.IEsSmsService;
import com.shopx.trade.web.constant.ApiStatus;
import com.shopx.trade.web.request.EsMemberRegistorForm;
import com.shopx.trade.web.request.MemberCheckCodeForm;
import com.shopx.trade.web.request.MemberFastLoginForm;
import com.shopx.trade.web.request.MemberLoginForm;
import com.shopx.trade.web.shiro.oath.ShiroKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.JedisCluster;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: LNS 1220316142@qq.com
 * @since: 2019/7/2 16:38
 */
@Api(value = "/member", tags = "用户登录模块")
@RestController
@RequestMapping("/member")
public class MemberLoginController {

    private static Logger logger = LoggerFactory.getLogger(MemberLoginController.class);

    @Autowired
    private Producer producer;
    @Autowired
    private JedisCluster jedisCluster;
    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberTokenService iEsMemberTokenService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000, check = false)
    private IEsMemberService memberService;

    @Reference(version = "${dubbo.application.version}", timeout = 5000,check = false)
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

    @ApiOperation(value = "获取图片验证码", notes = "获取图片验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "type", value = "验证码类型 登录：REGISTER，重置密码：MODIFY_PASSWORD 修改手机号：VALIDATE_MOBILE，绑定手机：BIND_MOBILE，找回密码：FIND_PASSWORD", dataType = "String", paramType = "query"),
            @ApiImplicitParam(name = "uuid", value = "唯一标识", required = true, dataType = "String", paramType = "query", example = "1234")
    })
    @GetMapping("/captcha.jpg")
    public void kaptcha(HttpServletResponse response, String type, String uuid) throws IOException {
        logger.info("前台请求的UUID:" + uuid);
        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("uuid不能为空");
        }

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

    @ApiOperation(value = "PC会员注册", notes = "依据前端传递的表单值新增会员")
    @PostMapping("/pcInsertMember")
    @ResponseBody
    public ApiResponse pcInsertMember(@RequestBody EsMemberRegistorForm esMemberForm, HttpServletRequest request) {
        //判断手机号格式
        if(!FormatValidateUtils.isMobile(esMemberForm.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        String mobileCode = jedisCluster.get(esMemberForm.getMobile() + "member");
        logger.info("session中短信码字符串:" + mobileCode);
//        Boolean bool = esMemberForm.getCode().equals(mobileCode);
//        if (!bool) {
//            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
//        }
        //获取用户ip地址
        String ip = IPUtil.getIpAddr(request);
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        BeanUtil.copyProperties(esMemberForm, esMemberDTO);
        if (null != ip) {
            esMemberDTO.setRegisterIp(ip);
        }
        //TODO 不要了？
        /*if (esMemberForm.getType() == MemberConstant.IsCommon) {
            esMemberDTO.setCompanyCode(null);
        }*/

        //判断手机号是否已注册
        DubboResult<EsMemberDO> resultMobile = memberService.getMemberInfoByMobile(esMemberForm.getMobile());
        if (resultMobile.isSuccess()) {
            if (resultMobile.getData() != null) {
                return ApiResponse.fail(MemberErrorCode.EXIST_MOBILE.getErrorCode(), MemberErrorCode.EXIST_MOBILE.getErrorMsg());
            }
        }

        //判断用户名是否已被使用
        DubboResult<EsMemberDO> resultName = memberService.getMemberInfoByName(esMemberForm.getName());
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
        DubboResult<EsMemberDO> result = memberService.insertMember(esMemberDTO);
        if (result.isSuccess()) {
            jedisCluster.del(esMemberForm.getMobile() + "member");
            return ApiResponse.success(result.getData());
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
        String mobileCode = jedisCluster.get(checkCodeForm.getMobile() + "member");
        if(!mobileCode.equals(checkCodeForm.getCode())){
            return ApiResponse.fail(MemberErrorCode.PARAM_ERROR.getErrorCode(), "短信验证码有误");
        }
        return ApiResponse.success();
    }



    /**
     * 登录
     */
    @ApiOperation(value = "登录", notes = "登录")
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody MemberLoginForm form) {
        logger.info("POST请求登录");
        if(StringUtils.isBlank(form.getNameOrMobile())){
            return ApiResponse.fail(MemberErrorCode.ACCOUNT_EMPTY.getErrorCode(), MemberErrorCode.ACCOUNT_EMPTY.getErrorMsg());
        }
        if (StringUtils.isBlank(form.getPassword())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_PASSWORD_NOT_EMPTY.getErrorCode(), MemberErrorCode.MEMBER_PASSWORD_NOT_EMPTY.getErrorMsg());
        }
        if (StringUtils.isBlank(form.getCaptcha())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_NOT_EMPTY.getErrorCode(), MemberErrorCode.MEMBER_CODE_NOT_EMPTY.getErrorMsg());
        }
        if (StringUtils.isBlank(form.getUuid())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_UUID_NOT_EMPTY.getErrorCode(), MemberErrorCode.MEMBER_UUID_NOT_EMPTY.getErrorMsg());
        }
        String validateCode = jedisCluster.get(form.getUuid());
        logger.info("session中的图形码字符串:" + validateCode);

        //比对
        if (StringUtils.isBlank(form.getCaptcha()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(form.getCaptcha())) {
            return ApiResponse.fail(MemberErrorCode.MEMBER_CODE_ERROR.getErrorCode(), MemberErrorCode.MEMBER_CODE_ERROR.getErrorMsg());
        }
//        EsMemberDTO esMemberDTO = new EsMemberDTO();
//        esMemberDTO.setNameOrMobile(form.getNameOrMobile());
        DubboResult<EsMemberDO> result = memberService.getMemberInfoByNameOrMobile(form.getNameOrMobile());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorMsg());
            }

            EsMemberDO userDo = result.getData();
            if(userDo.getState() == 1){
                return ApiResponse.fail(MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorCode(), MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorMsg());
            }
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
            // 清除验证码
            jedisCluster.del(form.getUuid());
            //生成token，并保存到数据库
            DubboResult esMemberDO = memberService.getMember(userDo.getId());
            if (!esMemberDO.isSuccess()) {
                return ApiResponse.fail(ApiStatus.wrapperException(esMemberDO));
            }
            EsMemberDO esMemberDO1 = (EsMemberDO) esMemberDO.getData();
            EsLoginMemberVO es = new EsLoginMemberVO();
            if (null != esMemberDO1) {
                if (null != esMemberDO1.getFace()) {
                    es.setFace(esMemberDO1.getFace());
                }
                es.setId(userDo.getId());
                es.setName(esMemberDO1.getName());
                if (null != esMemberDO1.getNickname()) {
                    es.setNickname(esMemberDO1.getNickname());
                }
            }
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
    @ApiOperation(value = "快捷登录", notes = "快捷登录")
    @PostMapping("/fastLogin")
    @ResponseBody
    public ApiResponse fastLogin(@RequestBody MemberFastLoginForm form) {
        logger.info("POST请求登录");
        //判断手机号格式
        if(StringUtils.isBlank(form.getMobile()) || !FormatValidateUtils.isMobile(form.getMobile())){
            return ApiResponse.fail(MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorCode(), MemberErrorCode.MOBILE_FORMAT_ERROR.getErrorMsg());
        }
        String mobielSign = form.getMobile() + "member";
        String code = jedisCluster.get(mobielSign);
        if ( StringUtils.isBlank(code) || !form.getCode().equals(code)) {
            return ApiResponse.fail(MemberErrorCode.ERROR_SMS_CODE.getErrorCode(), MemberErrorCode.ERROR_SMS_CODE.getErrorMsg());
        }
        EsMemberDTO esMemberDTO = new EsMemberDTO();
        esMemberDTO.setNameOrMobile(form.getMobile());
        DubboResult<EsMemberDO> result = memberService.getMemberInfoByNameOrMobile(esMemberDTO.getNameOrMobile());
        if (result.isSuccess()) {
            if (result.getData() == null) {
                return ApiResponse.fail(MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorCode(), MemberErrorCode.MEMBER_ACCOUNT_NOT_EXIST.getErrorMsg());
            }
            EsMemberDO memberDO = result.getData();
            if(memberDO.getState() == 1){
                return ApiResponse.fail(MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorCode(), MemberErrorCode.ACCOUNT_FORBIDDEN.getErrorMsg());
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


    @ApiOperation(value = "依据token查询用户所有信息", notes = "依据token查询用户所有信息")
    @GetMapping("/getInfoByToken")
    @ResponseBody
    public ApiResponse getAddressList(HttpServletRequest request) {
        String token = request.getHeader("token");
        if (null == token) {
            return ApiResponse.fail(MemberErrorCode.TOKEN_EMPTY.getErrorCode(), MemberErrorCode.TOKEN_EMPTY.getErrorMsg());
        }
        EsMemberTokenDTO esMemberTokenDTO = new EsMemberTokenDTO();
        esMemberTokenDTO.setToken(token);
        DubboResult tokenMember = iEsMemberTokenService.getMemberTokenInfo(esMemberTokenDTO);
        EsMemberTokenDO esMemberTokenDO = (EsMemberTokenDO) tokenMember.getData();
        DubboResult<EsMemberDO> esToken = memberService.getMember(esMemberTokenDO.getMemberId());
        EsMemberTokenInfoVO esMemberVO = new EsMemberTokenInfoVO();
        EsMemberDO esMemberDO = esToken.getData();
        BeanUtil.copyProperties(esMemberDO, esMemberVO);
        if (esToken.isSuccess()) {
            return ApiResponse.success(esMemberVO);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(esToken));
    }

    @ApiOperation(value = "发送短信验证码", notes = "发送短信验证码")
    @PostMapping("/sendSMSCode/{mobile}")
    @ResponseBody
    public ApiResponse sendSMSCode(String mobile) {
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
            String mobielSign = mobile + "member";
            jedisCluster.setex(mobielSign, SMSEXPIRE, code);
            return ApiResponse.success(code);
        }
        return ApiResponse.fail(ApiStatus.wrapperException(result));
    }
}