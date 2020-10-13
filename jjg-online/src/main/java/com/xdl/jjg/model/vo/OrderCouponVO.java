package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单下单时优惠券
 * @author LJG
 *
 */
@Data
@ApiModel( description = "订单下单时优惠券")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class OrderCouponVO implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5236361119763282449L;

	/**
	 * 记录下单时使用的会员优惠券ID
	 */
	@ApiModelProperty(value = "会员优惠卷id" )
	private Long memberCouponId;

	/**
	 * 记录下单时赠送的优惠券ID
	 */
	@ApiModelProperty(value = "优惠卷id" )
	private Long couponId;

	@ApiModelProperty(value = "卖家id" )
	private Long shopId;

	@ApiModelProperty(value = "金额" )
	private Double couponMoney;

	@ApiModelProperty(value = "有效期" )
	private Long endTime;

	@ApiModelProperty(value = "使用条件" )
	private String useTerm;

}
