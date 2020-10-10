package com.xdl.jjg.web.controller;

import com.google.code.kaptcha.Producer;
import com.xdl.jjg.model.domain.EsAdminUserDO;
import com.xdl.jjg.model.domain.EsAdminUserTokenDO;
import com.xdl.jjg.model.dto.EsAdminUserDTO;
import com.xdl.jjg.model.dto.EsAdminUserTokenDTO;
import com.xdl.jjg.model.enums.CachePrefix;
import com.xdl.jjg.model.form.EsLoginForm;
import com.xdl.jjg.model.form.EsUpdatePasswordForm;
import com.xdl.jjg.model.vo.EsAdminUserTokenVO;
import com.xdl.jjg.response.service.DubboResult;
import com.xdl.jjg.response.web.RestResult;
import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.util.BeanUtil;
import com.xdl.jjg.util.JwtUtils;
import com.xdl.jjg.util.StringUtil;
import com.xdl.jjg.web.service.IEsAdminUserService;
import com.xdl.jjg.web.service.IEsAdminUserTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  前端控制器-管理员登录相关API
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@RestController
@RequestMapping("/adminUser")
@Api(value="/adminUser", tags="管理员登录相关API")
public class EsAdminUserLoginController {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IEsAdminUserService iesAdminUserService;

    @Autowired
    private IEsAdminUserTokenService adminUserTokenService;

    @Autowired
    private Producer producer;

    @Autowired
    private RedisTemplate redisTemplate;

    //30分钟过期
    @Value("${zhuox.shiro.expire}")
    private int EXPIRE;

    @ApiOperation(value = "图片验证码")
    @GetMapping("/captcha.jpg")
    @ApiImplicitParam(name = "uuid", value = "前台请求的UUID", required = true, dataType = "String", paramType = "query")
    public void kaptcha(HttpServletResponse response , @RequestParam String uuid) throws IOException {
        logger.info("前台请求的UUID:" + uuid);
        if (StringUtils.isBlank(uuid)) {
            throw new RuntimeException("uuid不能为空");
        }
        //生成文字验证码
        String code = producer.createText();
        redisTemplate.opsForValue().set(CachePrefix.SYSTEM_KAPTCHA.getPrefix() + uuid,code);

        response.setHeader("Cache-Control", "no-store,no-cache");
        response.setContentType("image/jpeg");
        logger.info("code:" + code);
        BufferedImage image = producer.createImage(code);
        ServletOutputStream outputStream = response.getOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        IOUtils.closeQuietly(outputStream);
    }


    @ApiOperation(value = "登录")
    @PostMapping("/login")
    @ResponseBody
    public Object login(@Valid @RequestBody @ApiParam(name="登录信息form对象",value="form") EsLoginForm form) {
        logger.info("POST请求登录");
        String validateCode = (String)redisTemplate.opsForValue().get(CachePrefix.SYSTEM_KAPTCHA.getPrefix() + form.getUuid());
        logger.info("session中的图形码字符串:" + validateCode);
        //比对
        if (StringUtils.isBlank(form.getCaptcha()) || StringUtils.isBlank(validateCode) || !validateCode.equalsIgnoreCase(form.getCaptcha())) {
            return RestResult.fail(105,"验证码错误!");
        }
        EsAdminUserDTO adminUserDTO = new EsAdminUserDTO();
        adminUserDTO.setUsername(form.getUsername());
        DubboResult<EsAdminUserDO> result = iesAdminUserService.getUserInfo(adminUserDTO);
        if(result.isSuccess()){
            if(result.getData() == null){
                return RestResult.fail(106,"账号不存在!");
            }
            EsAdminUserDO adminUserDO = result.getData();
            if (!adminUserDO.getPassword().equals(StringUtil.md5(form.getPassword() + adminUserDO.getSalt()))) {
                return RestResult.fail(106,"密码不正确!");
            }

            // 清除验证码
            redisTemplate.delete(CachePrefix.SYSTEM_KAPTCHA.getPrefix() + form.getUuid());

            //生成token，并保存到数据库
            return createToken(adminUserDO);
        }
        return RestResult.fail(104,"登录失败!");
    }

    //生成token，并保存到数据库
    public Object createToken(EsAdminUserDO adminUserDO) {
        //判断是否生成过token
        EsAdminUserTokenDTO esAdminUserTokenDTO = new EsAdminUserTokenDTO();
        esAdminUserTokenDTO.setUserId(adminUserDO.getId());
        DubboResult<EsAdminUserTokenDO> result = adminUserTokenService.getUserToken(esAdminUserTokenDTO);
        if(result.isSuccess()){
            EsAdminUserTokenDO userTokenDO = result.getData();
            //当前时间
            Date now = new Date();
            //过期时间
            Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
            //生成一个token
            Map<String, Object> params = new HashMap<>();
            params.put("token", adminUserDO.getId());
            String token = JwtUtils.generateToken(params, "zhuoxdubbo", EXPIRE);

            if (userTokenDO == null) {
                esAdminUserTokenDTO = new EsAdminUserTokenDTO();
                esAdminUserTokenDTO.setUserId(adminUserDO.getId());
                esAdminUserTokenDTO.setToken(token);
                esAdminUserTokenDTO.setUpdateTime(now.getTime());
                esAdminUserTokenDTO.setExpireTime(expireTime.getTime());
                //保存token
                adminUserTokenService.insertAdminUserToken(esAdminUserTokenDTO);

                EsAdminUserTokenVO adminUserTokenVO = new EsAdminUserTokenVO();
                adminUserTokenVO.setToken(token);
                adminUserTokenVO.setUserId(adminUserDO.getId());
                adminUserTokenVO.setFace(adminUserDO.getFace());
                adminUserTokenVO.setRoleId(adminUserDO.getRoleId());
                return RestResult.ok(adminUserTokenVO);
            } else {
                if(userTokenDO.getExpireTime() < System.currentTimeMillis()){
                    esAdminUserTokenDTO.setToken(token);
                }else{
                    esAdminUserTokenDTO.setToken(userTokenDO.getToken());
                }
                esAdminUserTokenDTO.setUpdateTime(now.getTime());
                esAdminUserTokenDTO.setExpireTime(expireTime.getTime());
                //更新token
                adminUserTokenService.updateAdminUserToken(esAdminUserTokenDTO);

                EsAdminUserTokenVO adminUserTokenVO = new EsAdminUserTokenVO();
                adminUserTokenVO.setToken(esAdminUserTokenDTO.getToken());
                adminUserTokenVO.setUserId(adminUserDO.getId());
                adminUserTokenVO.setFace(adminUserDO.getFace());
                adminUserTokenVO.setRoleId(adminUserDO.getRoleId());
                return RestResult.ok(adminUserTokenVO);
            }
        }
        return RestResult.fail(104,"登录失败!");
    }


    @ApiOperation(value = "退出")
    @PostMapping("/logout")
    @ResponseBody
    public Object logout() {
        //生成一个token
        JwtUtils jwtUtils = new JwtUtils();
        Map<String, Object> params = new HashMap<>();
        params.put("token", ShiroKit.getUser().getId());
        String token = jwtUtils.generateToken(params, "zhuoxdubbo", EXPIRE);
        //修改token
        EsAdminUserTokenDTO esAdminUserTokenDTO = new EsAdminUserTokenDTO();
        esAdminUserTokenDTO.setUserId(ShiroKit.getUser().getId());
        esAdminUserTokenDTO.setToken(token);
        adminUserTokenService.updateAdminUserToken(esAdminUserTokenDTO);
        //清除缓存
        ShiroKit.getSubject().logout();
        return RestResult.ok();
    }

    @PutMapping(value = "/updatePassword")
    @ApiOperation(value = "修改管理员密码及头像")
    @ResponseBody
    public Object updatePassword(@RequestBody @ApiParam(name="密码修改form对象",value="form") @Valid EsUpdatePasswordForm form, HttpServletRequest request) {
        Long uid = ShiroKit.getUser().getId();
        DubboResult<EsAdminUserDO> result = iesAdminUserService.getById(uid);
        EsAdminUserDO esAdminUserDO = result.getData();
        if (esAdminUserDO == null) {
            return RestResult.fail(201,"当前管理员不存在");
        }
        
        EsAdminUserDTO esAdminUserDTO = new EsAdminUserDTO();
        BeanUtil.copyProperties(esAdminUserDO, esAdminUserDTO);

        //校验密码
        if (!StringUtil.isEmpty(form.getOldPassword()) && StringUtil.isEmpty(form.getPassword())) {
            return RestResult.fail(202,"新密码不能为空");
        }
        if (StringUtil.isEmpty(form.getOldPassword()) && !StringUtil.isEmpty(form.getPassword())) {
            return RestResult.fail(203,"原始密码不能为空");
        }
        if (!StringUtil.isEmpty(form.getOldPassword()) && !StringUtil.isEmpty(form.getPassword())) {
            String dbOldPassword = StringUtil.md5(form.getOldPassword() + esAdminUserDO.getSalt());
            if (!dbOldPassword.equals(esAdminUserDO.getPassword())) {
                return RestResult.fail(204,"原密码错误");
            }
            esAdminUserDTO.setPassword(form.getPassword());
        } else {
            esAdminUserDTO.setPassword("");
        }
        esAdminUserDTO.setFace(form.getFace());
        DubboResult dubboResult = iesAdminUserService.updateEsAdminUser(esAdminUserDTO);
        if (dubboResult.isSuccess()) {
            //退出
            logout();
            return RestResult.ok();
        } else {
            return RestResult.fail(dubboResult.getMsg());
        }
    }
}
