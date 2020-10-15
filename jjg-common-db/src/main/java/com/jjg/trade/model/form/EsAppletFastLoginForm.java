package com.jjg.trade.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * applet登录表单
 */
@Data
@ApiModel
public class EsAppletFastLoginForm implements Serializable {
    private static final long serialVersionUID = 3781140078146967600L;
    /**
     * 手机号
     */
    @ApiModelProperty(required = true,value = "手机号")
    @NotBlank(message = "手机号不能为空")
    private String mobile;

    /**
     * 验证码
     */
    @ApiModelProperty(required = true,value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    /**
     * 临时登录凭证
     */
    @ApiModelProperty(required = true,value = "临时登录凭证")
    @NotBlank(message = "临时登录凭证不能为空")
    private String code;

}
