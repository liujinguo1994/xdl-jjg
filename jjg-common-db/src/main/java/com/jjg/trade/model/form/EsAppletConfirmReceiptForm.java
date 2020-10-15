package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * 小程序-确认收货
 */
@Data
@ApiModel
public class EsAppletConfirmReceiptForm implements Serializable {

    private static final long serialVersionUID = -6389742728556211209L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "订单号")
    @NotBlank(message = "订单号")
    private String orderSn;

}
