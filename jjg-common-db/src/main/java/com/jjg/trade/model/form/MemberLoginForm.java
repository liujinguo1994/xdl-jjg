package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 */
@Data
public class MemberLoginForm {
    /**
     * 登陆用户名
     */
    @ApiModelProperty(required = false, value = "登陆用户名")
    private String username;
    /**
     * 登陆密码
     */
    @ApiModelProperty(required = false,value = "登陆密码")
    private String password;
    /**
     * uuid
     */
    @ApiModelProperty(required = false,value = "uuid")
    private String uuid;
    /**
     * 手机号或者用户名
     */
    @ApiModelProperty(required = false,value = "手机号或者用户名")
    private String nameOrMobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = false,value = "验证码")
    private String captcha;
}
