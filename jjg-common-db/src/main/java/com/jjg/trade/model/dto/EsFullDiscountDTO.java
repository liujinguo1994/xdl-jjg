package com.jjg.member.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 满减满赠-es_full_discount
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsFullDiscountDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
	private Long id;

    /**
     * 优惠门槛金额
     */
	private Double fullMoney;

    /**
     * 减现金
     */
	private Double minusValue;

    /**
     * 活动是否减现金
     */
	private Integer isFullMinus;

    /**
     * 是否免邮
     */
	private Integer isFreeShip;

    /**
     * 是否有赠品
     */
	private Integer isSendGift;

    /**
     * 是否赠优惠券
     */
	private Integer isSendBonus;

    /**
     * 赠品id(支持多赠品)
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long giftId;

    /**
     * 优惠券id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long bonusId;

    /**
     * 是否打折
     */
	private Integer isDiscount;

    /**
     * 折扣
     */
	private Double discountValue;

    /**
     * 创建时间
     */
	private Long createTime;

    /**
     * 更新时间
     */
	private Long updateTime;

    /**
     * 活动标题
     */
	private String title;

    /**
     * 商品参与方式，全部商品：1，部分商品：2
     */
	private Integer rangeType;

    /**
     * 是否停用
     */
	private Integer isDel;

    /**
     * 活动说明
     */
	private String description;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

    /**
     * 商家名称
     */
	private String shopName;

	/**
	 * 活动开始时间
	 */
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	private Long endTime;

	/**
	 * 商品集合
	 */
	private List<EsPromotionGoodsDTO> goodsList;

	protected Serializable pkVal() {
		return this.id;
	}

}
