package com.xdl.jjg.model.form;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;


/**
 * 登录表单
 */
@Data
@Api
public class MemberLoginForm {

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String name;

    @ApiModelProperty(value = "密码")
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String captcha;

    @ApiModelProperty(value = "UUID")
    @NotBlank(message = "UUID不能为空")
    private String uuid;


}
