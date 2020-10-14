package com.jjg.member.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 会员优惠券管理
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsCouponManageMementDO implements Serializable {


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
     * 订单编号
     */
	private String orderSn;
    /**
     * 优惠券名称
     */
	private String title;
    /**
     * 优惠券面额
     */
	private Double couponMoney;
    /**
     * 使用起始时间
     */
	private Long startTime;
    /**
     * 使用状态 1待使用，2已使用
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
     * 是否过期(1未过期，2已过期)
     */
    private Integer overdueState;

    private String overdueStateText;

    private String stateText;
}
