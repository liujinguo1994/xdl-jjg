package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 小程序-再次购买
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-01
 */
@Data
@ApiModel
public class EsAppletBuyAgainForm implements Serializable {
    private static final long serialVersionUID = 7089007998510434957L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "订单号")
    @NotBlank(message = "订单号不能为空")
    private String orderSn;

}
