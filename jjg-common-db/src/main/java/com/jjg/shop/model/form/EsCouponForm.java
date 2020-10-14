package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Api
@Data
public class EsCouponForm implements Serializable {

    @ApiModelProperty(value = "优惠券名称")
    @NotBlank
    private String title;

    @ApiModelProperty(value = "优惠券面额")
    private Double couponMoney;

    @ApiModelProperty(value = "优惠券门槛价格")
    @NotNull
    private Double couponThresholdPrice;

    @ApiModelProperty(value = "使用起始时间")
    @NotNull
    private Long startTime;

    @ApiModelProperty(value = "使用结束时间")
    @NotNull
    private Long endTime;

    @ApiModelProperty(value = "发行量")
    @NotNull
    private Integer createNum;

    @ApiModelProperty(value = "每人限领数量")
    @NotNull
    private Integer limitNum;

    @ApiModelProperty(value = "是否允许被领取 1 允许 2不允许")
    private Integer isReceive;

    @ApiModelProperty(value = "有效天数")
    private Integer validDay;

    @ApiModelProperty(value = "优惠券类型 ")
    @NotBlank
    private String couponType;
    @ApiModelProperty(value = "是否支持促销商品使用true false ")
    private Boolean isProGoods;
    /**
     * 是否为赠券 1 是2 否
     */
    @ApiModelProperty(value = "是否为赠券 1 是2 否 ")
    private Integer isCoupons;
    @ApiModelProperty(value = "是否是注册赠券 1 是 2 不是")
    private Integer isRegister;

}
