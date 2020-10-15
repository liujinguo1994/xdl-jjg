package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-校验短信验证码
 * </p>
 */
@Data
@ApiModel
public class EsAppletCheckSmsCodeForm implements Serializable {


    private static final long serialVersionUID = 6175566214559738099L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true, value = "验证码")
    @NotBlank(message = "验证码不能为空")
    private String smsCode;

}
