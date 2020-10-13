package com.xdl.jjg.model.form;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 */
@Data
public class MemberFastLoginForm {
    /**
     * 手机号或者用户名
     */
    @ApiModelProperty(required = true,value = "手机号")
    private String mobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = true,value = "验证码")
    private String code;

}
