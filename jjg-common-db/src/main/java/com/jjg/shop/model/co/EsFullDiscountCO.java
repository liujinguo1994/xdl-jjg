package com.xdl.jjg.model.co;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 满减满赠表VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsFullDiscountCO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 活动id
     */
	@ApiModelProperty(value = "活动id")
	private Long id;

    /**
     * 优惠门槛金额
     */
	@ApiModelProperty(value = "优惠门槛金额")
	private Double fullMoney;

    /**
     * 减现金
     */
	@ApiModelProperty(value = "减现金")
	private Double minusValue;

    /**
     * 活动是否减现金
     */
	@ApiModelProperty(value = "活动是否减现金")
	private Integer isFullMinus;

    /**
     * 是否免邮
     */
	@ApiModelProperty(value = "是否免邮")
	private Integer isFreeShip;

    /**
     * 是否有赠品
     */
	@ApiModelProperty(value = "是否有赠品")
	private Integer isSendGift;

    /**
     * 是否赠优惠券
     */
	@ApiModelProperty(value = "是否赠优惠券")
	private Integer isSendBonus;

    /**
     * 赠品id(支持多赠品)
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "赠品id(支持多赠品)")
	private Long giftId;

    /**
     * 优惠券id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "优惠券id")
	private Long bonusId;

    /**
     * 是否打折
     */
	@ApiModelProperty(value = "是否打折")
	private Integer isDiscount;

    /**
     * 折扣
     */
	@ApiModelProperty(value = "折扣")
	private Double discountValue;

    /**
     * 创建时间
     */
	@ApiModelProperty(value = "活动开始时间")
	private Long createTime;

    /**
     * 更新时间
     */
	@ApiModelProperty(value = "活动结束时间")
	private Long updateTime;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

    /**
     * 商品参与方式，全部商品：1，部分商品：2
     */
	@ApiModelProperty(value = "商品参与方式，全部商品：1，部分商品：2")
	private Integer rangeType;

    /**
     * 是否停用
     */
	@ApiModelProperty(value = "是否停用")
	private Integer isDel;

    /**
     * 活动说明
     */
	@ApiModelProperty(value = "活动说明")
	private String description;

    /**
     * 商家id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "商家id")
	private Long shopId;

    /**
     * 商家名称
     */
	@ApiModelProperty(value = "商家名称")
	private String shopName;

	/**
	 * 满减赠品
	 */
	@ApiModelProperty(value = "满减赠品")
	private EsFullDiscountGiftCO fullDiscountGift;

	/**
	 * 满减赠优惠券
	 */
	@ApiModelProperty(value = "满减赠优惠券")
	private EsCouponCO coupon;

	/**
	 * 活动开始时间
	 */
	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

	/**
	 * 活动结束时间
	 */
	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

	@ApiModelProperty(value = "活动状态")
	private String  statusText;

    @ApiModelProperty(value = "赠品信息")
    private EsFullDiscountGiftCO esFullDiscountGiftCO;

    @ApiModelProperty(value = "赠品优惠券信息")
    private EsCouponCO esCouponCO;

	@ApiModelProperty(value = "活动状态标识,expired表示已失效")
	private String status;

	protected Serializable pkVal() {
		return this.id;
	}

}
