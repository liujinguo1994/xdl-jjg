package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 优惠券Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsCouponForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 优惠券名称
     */
    @ApiModelProperty(value = "优惠券名称")
    private String title;

    /**
     * 优惠券面额
     */
    @ApiModelProperty(value = "优惠券面额")
    private Double couponMoney;

    /**
     * 优惠券门槛价格
     */
    @ApiModelProperty(value = "优惠券门槛价格")
    private Double couponThresholdPrice;

    /**
     * 使用起始时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "使用起始时间")
    private Long startTime;

    /**
     * 使用截止时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "使用截止时间")
    private Long endTime;

    /**
     * 发行量
     */
    @ApiModelProperty(value = "发行量")
    private Integer createNum;

    /**
     * 每人限领数量
     */
    @ApiModelProperty(value = "每人限领数量")
    private Integer limitNum;

    /**
     * 店铺ID
     */
    @NotNull(message = "店铺id不能为空")
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "店铺ID")
    private Long shopId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;

    /**
     * 0正常，1下架
     */
    @ApiModelProperty(value = "0正常，1下架")
    private Integer isDel;

    /**
     * 是否允许领取
     */
    @ApiModelProperty(value = "是否允许领取")
    private Integer isReceive;

    /**
     * 有效天数
     */
    @ApiModelProperty(value = "有效天数")
    private Integer validDay;

}
