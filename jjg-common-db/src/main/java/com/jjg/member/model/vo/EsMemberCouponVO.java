package com.jjg.member.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:58
 */
@Data
@ApiModel
public class EsMemberCouponVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
	/**
	 * 优惠券类型
	 */
	@ApiModelProperty("优惠券类型")
	private String couponType;
    /**
     * 会员ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "会员ID")
	private Long memberId;

    /**
     * 优惠券表主键 (es_coupon)
     */
	@ApiModelProperty(value = "优惠券表主键 (es_coupon)")
	private Long couponId;

    /**
     * 使用时间
     */
	@ApiModelProperty(value = "使用时间")
	private Long updateTime;

    /**
     * 领取时间
     */
	@ApiModelProperty(value = "领取时间")
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
	@ApiModelProperty(value = "会员昵称")
	private String memberName;

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
     * 优惠券门槛金额
     */
	@ApiModelProperty(value = "优惠券门槛金额")
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
     * 使用状态
     */
	@ApiModelProperty(value = "使用状态:1未使用，2已使用，3失效")
	private Integer state;

    /**
     * 商家ID,优惠券属于哪个商家
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家ID,优惠券属于哪个商家")
	private Long shopId;

    /**
     * 商家店名
     */
	@ApiModelProperty(value = "商家店名")
	private String shopName;

    /**
     * 0正常，1下架
     */
	@ApiModelProperty(value = "0正常，1下架")
	private Integer isDel;

	/**
	 * 是否选中
	 */
	@ApiModelProperty(value = "是否选中 1 选中 ，2 未选中")
	private Integer isCheck;

	/**
	 *是否即将过期
	 */
	@ApiModelProperty(value = "是否即将过期")
	private Boolean judgmentToExpire;


}
