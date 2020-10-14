package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.common.util.BeanUtil;
import com.shopx.common.util.StringUtil;
import com.shopx.trade.api.model.enums.PromotionTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName: SeckillGoodsVO
 * @Description: 活动的集合
 * @Author: libw  981087977@qq.com
 * @Date: 6/17/2019 15:03
 * @Version: 1.0
 */
@Data
@ApiModel(description = "活动的集合")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradePromotionGoodsVO implements Serializable {

	private static final long serialVersionUID = -6422522292057193992L;

	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

	@ApiModelProperty(value = "活动id")
	private Long activityId;

	@ApiModelProperty(value = "活动工具类型")
	private String promotionType;

	@ApiModelProperty(value = "活动名称")
	private String title;

	@ApiModelProperty(value = "是否选中参与这个活动, 2为未选中 1为选中")
	private Integer isCheck;

	@ApiModelProperty(value = "满优惠活动")
	private EsFullDiscountVO fullDiscount;

	@ApiModelProperty(value = "满赠的赠品VO")
	private EsFullDiscountGiftVO fullDiscountGift;

	@ApiModelProperty(value = "单品立减活动")
	private EsMinusVO minus;

	@ApiModelProperty(value = "第二件半价活动")
	private EsHalfPriceVO halfPrice;

	@ApiModelProperty(value = "限时抢购活动")
	private SeckillGoodsVO seckillGoods;

	@ApiModelProperty(value = "公司折扣")
	private Double discount;

	@ApiModelProperty(value = "商品折扣")
	private Double goodsDiscounts;
    /**
     * 有些活动,有单独的活动数量，此数量为剩余的活动数量。
     */
    @ApiModelProperty(value = "剩余售空数量")
    private Integer soldOutCount;


	// V_2 Start
    @ApiModelProperty(value = "优惠满足门槛条件")
    private Double preferentialThreshold;
    @ApiModelProperty(value = "优惠价格")
    private Double preferentialPrice;
    @ApiModelProperty(value = "优惠折扣")
    private Double preferentialDiscount;
    @ApiModelProperty(value = "满赠品图片")
    private String giftImg;
    @ApiModelProperty(value = "满赠券面额")
    private Double couponMoney;
    @ApiModelProperty(value = "满额 是否免邮")
    private Integer isFreeShip;
    @ApiModelProperty(value = "优惠类型 用于满减活动(不满足优惠：Non,满减：FullMinus，满折:FullDiscount,满免邮：FullFreeShip，满赠品：FullSendGift,满赠券：FullSendBonus)")
    private String preferentialType;

    @ApiModelProperty(value = "活动内容（如满100减10）")
    public String getContent(){
        if(StringUtil.isEmpty(promotionType))return title;
        // 满减满赠
        if(StringUtil.equals(promotionType,PromotionTypeEnum.FULL_DISCOUNT.name())){
            if(StringUtil.equals(preferentialType,"FullMinus")){
                return String.format("满%s减%s",preferentialThreshold,fullDiscount.getMinusValue());
            }else if(StringUtil.equals(preferentialType,"FullDiscount")){
                return String.format("满%s打%s折",preferentialThreshold,fullDiscount.getDiscountValue());
            }else if(StringUtil.equals(preferentialType,"FullFreeShip")){
                return String.format("满%s包邮",preferentialThreshold);
            }else if(StringUtil.equals(preferentialType,"FullSendGift")){
                return String.format("满%s得赠品，赠完即止",preferentialThreshold);
            } else if(StringUtil.equals(preferentialType,"FullSendBonus")){
                return String.format("满%s得%s优惠券",preferentialThreshold,couponMoney);
            }
            // 单品立减
        }else if(StringUtil.equals(promotionType,PromotionTypeEnum.MINUS.name())){
            return String.format("下单立减%s",minus.getSingleReductionValue());
            //第二件半价
        }else if(StringUtil.equals(promotionType,PromotionTypeEnum.HALF_PRICE.name())){
            return "下单享受第二件半价";
            // 商品折扣
        }else if(StringUtil.equals(promotionType,PromotionTypeEnum.GOODS_DISCOUNT.name())){
            return String.format("下单商品打%s折",goodsDiscounts*10);
        }else if(StringUtil.equals(promotionType,PromotionTypeEnum.SECKILL.name())){
            return String.format("限时抢购，抢购价%s",seckillGoods.getSeckillPrice());
        }
        return this.title;
    }

	public TradePromotionGoodsVO(){
	    this.setIsCheck(2);
    }
    // 组装活动列表
    public TradePromotionGoodsVO(TradePromotionGoodsVO tradePromotionGoodsVO1){

        BeanUtil.copyProperties(tradePromotionGoodsVO1,this);
        if (PromotionTypeEnum.FULL_DISCOUNT.name().equals(tradePromotionGoodsVO1.getPromotionType())){
            EsFullDiscountVO fullDiscount = tradePromotionGoodsVO1.getFullDiscount();
            this.preferentialThreshold = fullDiscount.getFullMoney();
            if (fullDiscount.getIsFullMinus() == 1){
                this.preferentialType = "FullMinus";
                this.preferentialPrice = fullDiscount.getMinusValue();
            }
            if (fullDiscount.getIsDiscount() == 1){
                this.preferentialType = "FullDiscount";
                this.preferentialDiscount = fullDiscount.getDiscountValue();
            }
            if (fullDiscount.getIsSendGift() == 1){
                this.preferentialType = "FullSendGift";
                this.giftImg = fullDiscount.getFullDiscountGift().getGiftImg();
            }
            if (fullDiscount.getIsSendBonus() == 1){
                this.preferentialType = "FullSendBonus";
                this.couponMoney = fullDiscount.getCoupon().getCouponMoney();
            }
            if (fullDiscount.getIsFreeShip() == 1){
                this.preferentialType = "FullFreeShip";
                this.isFreeShip = fullDiscount.getIsFreeShip();
            }
        }
        this.setIsCheck(2);
    }
    // V_2 end
}
