package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置支付类型
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-29
 */
@Data
@ApiModel
public class EsAppletSetPaymentTypeForm implements Serializable {
    private static final long serialVersionUID = -7152797799633237811L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "支付类型 在线支付：ONLINE，货到付款：COD")
    @NotBlank(message = "支付类型不能为空")
    private String paymentType;

}
