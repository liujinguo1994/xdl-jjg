package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsSellerCouponDTO implements Serializable {

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long startTime;

    /**
     * 使用截止时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
	@JsonSerialize(using = ToStringSerializer.class)
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
	 * 优惠券状态 0 表示 未开始，1 进行中，2 已结束
	 */
	private String couponStatus;
	/**
	 * 是否为赠券 1 是2 否
	 */
	private Integer isCoupons;
	/**
	 * 是否支持促销商品 0 支持 1 不支持
	 */
	private Boolean isProGoods;
	private Integer isGoods;
	private Integer isRegister;
	protected Serializable pkVal() {
		return this.id;
	}

}
