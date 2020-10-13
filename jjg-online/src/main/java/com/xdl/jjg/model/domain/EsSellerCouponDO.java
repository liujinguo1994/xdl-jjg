package com.xdl.jjg.model.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 优惠卷
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsSellerCouponDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 优惠券名称
     */
	private String title;

    /**
     * 优惠券面额
     */
	private Double couponMoney;

    /**
     * 优惠券门槛价格
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
     * 发行量
     */
	private Integer createNum;

    /**
     * 每人限领数量
     */
	private Integer limitNum;

    /**
     * 已被使用的数量
     */
	private Integer usedNum;

    /**
     * 店铺ID
     */
	private Long shopId;

    /**
     * 已被领取的数量
     */
	private Integer receivedNum;

    /**
     * 店铺名称
     */
	private String sellerName;

    /**
     * 0正常，1下架
     */
	private Integer isDel;

    /**
     * 是否允许领取
     */
	private Integer isReceive;

    /**
     * 有效天数
     */
	private Integer validDay;
	/**
	 * 优惠券类型
	 */
	private String couponType;

	/**
	 * 优惠券类型
	 */
	private String couponTypeText;

	private Boolean isProGoods;
	/**
	 * 商城首页使用 1是 0否 是否已领取
	 */
	private Integer isReceived;
	/**
	 * 是否为赠券
	 */
	private Integer isCoupons;
	/**
	 * 优惠券状态
	 */
	private  String couponStatus;
	private Integer isGoods;
	private Integer isRegister;

}
