package com.jjg.trade.model.form;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 登录表单
 */
@Data
@Api
public class MemberCheckCodeForm {
    /**
     * 手机号或者用户名
     */
    @ApiModelProperty(required = false,value = "手机号或者用户名")
    private String mobile;
    /**
     * 验证码
     */
    @ApiModelProperty(required = false,value = "验证码")
    private String code;
}
