package com.jjg.member.model.form;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 */
@Data
public class MemberCheckForm {
    /**
     * uuid
     */
    @ApiModelProperty(required = true,value = "uuid")
    private String uuid;
    /**
     * 手机号或者用户名
     */
    @ApiModelProperty(required = true,value = "手机号")
    private String nameOrMobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = true,value = "验证码")
    private String captcha;
}
