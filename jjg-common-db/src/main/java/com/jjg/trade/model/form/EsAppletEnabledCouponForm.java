package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-获取可用优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Data
@ApiModel
public class EsAppletEnabledCouponForm implements Serializable {
    private static final long serialVersionUID = 8295222345562524256L;

    @ApiModelProperty(required = true,value = "支付金额")
    @NotNull(message = "支付金额不能为空")
    private Double payMoney;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;
}
