package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-设置优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-03
 */
@Data
@ApiModel
public class EsAppletSetCouponForm implements Serializable {

    private static final long serialVersionUID = 3863114069985817704L;

    @ApiModelProperty(value = "店铺id")
    @NotNull(message = "店铺id不能为空")
    private Long shopId;

    @ApiModelProperty(value = "优惠卷id")
    @NotNull(message = "优惠卷id不能为空")
    private Long couponId;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

}
