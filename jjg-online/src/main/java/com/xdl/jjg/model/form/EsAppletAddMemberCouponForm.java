package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 小程序-新增会员优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Data
@ApiModel
public class EsAppletAddMemberCouponForm implements Serializable {
    private static final long serialVersionUID = -5672770383191553853L;

    @ApiModelProperty(required = true,value = "登录态标识")
    @NotBlank(message = "登录态标识不能为空")
    private String skey;

    @ApiModelProperty(required = true,value = "优惠券ID")
    @NotNull(message = "优惠券ID不能为空")
    private Long couponId;
}
