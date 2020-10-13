package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券QueryForm
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-10-16 15:08:19
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsMemberCouponQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(required = true, value = "会员ID")
	private Long memberId;

    /**
     * 优惠券表主键 (es_coupon)
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(required = true, value = "优惠券表主键 (es_coupon)")
	private Long couponId;

    /**
     * 使用时间
     */
	@ApiModelProperty(value = "使用时间")
	private Long updateTime;

    /**
     * 领取时间
     */
	@ApiModelProperty(required = true, value = "领取时间")
	private Long createTime;

    /**
     * 订单主键
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "订单主键")
	private Long orderId;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 会员昵称
     */
	@ApiModelProperty(required = true, value = "会员昵称")
	private String memberName;

    /**
     * 优惠券名称
     */
	@ApiModelProperty(required = true, value = "优惠券名称")
	private String title;

    /**
     * 优惠券面额
     */
	@ApiModelProperty(required = true, value = "优惠券面额")
	private Double couponMoney;

    /**
     * 优惠券门槛金额
     */
	@ApiModelProperty(required = true, value = "优惠券门槛金额")
	private Double couponThresholdPrice;

    /**
     * 使用起始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(required = true, value = "使用起始时间")
	private Long startTime;

    /**
     * 使用截止时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(required = true, value = "使用截止时间")
	private Long endTime;

    /**
     * 使用状态(1:正常，2:已使用，3失效)
     */
	@ApiModelProperty(required = true, value = "使用状态(1:正常，2:已使用，3失效)")
	private Integer state;

    /**
     * 商家ID,优惠券属于哪个商家
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(required = true, value = "商家ID,优惠券属于哪个商家")
	private Long shopId;

    /**
     * 商家店名
     */
	@ApiModelProperty(required = true, value = "商家店名")
	private String shopName;

    /**
     * 0正常，1下架
     */
	@ApiModelProperty(required = true, value = "0正常，1下架")
	private Integer isDel;

}
