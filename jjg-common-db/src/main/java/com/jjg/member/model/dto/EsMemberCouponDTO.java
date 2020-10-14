package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券
 * </p>
 *
 * @author lins 1220316142@qq.com mvn clean package -DskipTest -U
 * @since 2019-06-04
 */
@Data
@ToString
public class EsMemberCouponDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 消费类型
     */
    private String type;

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
     * 使用状态1:未使用, 3:失效, 2已用
     * 使用状态 1:未使用, 2已用
     */
	private Integer state;
    /**
     * 是否过期(1未过期，2已过期)
     */
    private Integer overdueState;

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
    /**
     * 输入框输入值(优惠券编号，名称，关联订单号)
     */
    private String keyword;
    /**
     * 当前时间
     */
    private Long timesNow;
    /**
     * 上下架 0正常，1下架
     */
    private Integer isDel;


}
