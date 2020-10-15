package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Api
public class EsFullDiscountForm implements Serializable {
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
    @ApiModelProperty(value = "活动是否减现金 1 是 0 不是")
    private Integer isFullMinus;

    /**
     * 是否免邮
     */
    @ApiModelProperty(value = "是否免邮 1 是 0不是")
    private Integer isFreeShip;

    /**
     * 是否有赠品
     */
    @ApiModelProperty(value = "是否有赠品 1 是 0不是")
    private Integer isSendGift;

    /**
     * 是否赠优惠券
     */
    @ApiModelProperty(value = "是否赠优惠券 1 是 0不是")
    private Integer isSendBonus;

    /**
     * 赠品id(支持多赠品)
     */
    @ApiModelProperty(value = "赠品id(支持多赠品)")
    private Long giftId;

    /**
     * 优惠券id
     */
    @ApiModelProperty(value = "优惠券id")
    private Long bonusId;

    /**
     * 是否打折
     */
    @ApiModelProperty(value = "是否打折 1 是 0不是")
    private Integer isDiscount;

    /**
     * 折扣
     */
    @ApiModelProperty(value = "折扣")
    private Double discountValue;

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
     * 活动说明
     */
    @ApiModelProperty(value = "活动说明")
    private String description;

    @ApiModelProperty(value = "参与活动商品集合")
    private List<EsPromotionGoodsForm> goodsList;
}
