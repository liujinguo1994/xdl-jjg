package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberTradeCouponDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 会员ID
     */
	private Long memberId;

    /**
     * 优惠券表主键 (es_coupon)
     */
	private Long couponId;

    /**
     * 使用时间
     */
	private Long updateTime;

    /**
     * 领取时间
     */
	private Long createTime;

    /**
     * 订单主键
     */
	private Long orderId;

    /**
     * 订单编号
     */
	private String orderSn;

    /**
     * 会员昵称
     */
	private String memberName;

    /**
     * 优惠券名称
     */
	private String title;

    /**
     * 优惠券面额
     */
	private Double couponMoney;

    /**
     * 优惠券门槛金额
     */
	private Double couponThresholdPrice;

    /**
     * 使用起始时间
     */
	private Long startTime;

    /**
     * 使用截止时间
     */
	private Long endTime;

    /**
     * 使用状态0:未使用, 1:失效, 2已用
     */
	private Integer state;

    /**
     * 商家ID,优惠券属于哪个商家
     */
	private Long shopId;

    /**
     * 商家店名
     */
	private String shopName;
    /**
     * 系统当前时间
     */
    private Long currentTime;


}
