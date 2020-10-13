package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 活动的Vo
 * @author Snow
 * @since v6.4
 * @version v1.0
 * 2017年08月24日14:48:36
 */
@Data
@ApiModel(description = "购物车中活动DTO")
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradePromotionGoodsDTO implements Serializable {

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

	@ApiModelProperty(value = "是否选中参与这个活动,0为未选中 1为选中")
	private Integer isCheck;
	@ApiModelProperty(value = "满优惠活动")
	private EsFullDiscountDTO fullDiscount;

	@ApiModelProperty(value = "满赠的赠品VO")
	private EsFullDiscountGiftDTO fullDiscountGift;

	@ApiModelProperty(value = "单品立减活动")
	private EsMinusDTO minus;

	@ApiModelProperty(value = "第二件半价活动")
	private EsHalfPriceDTO halfPrice;

	@ApiModelProperty(value = "限时抢购活动")
	private SeckillGoodsDTO seckillGoods;

	@ApiModelProperty(value = "公司折扣")
	private Double discount;

	@ApiModelProperty(value = "商品折扣")
	private Double goodsDiscounts;

	/**
	 * 有些活动,有单独的活动数量，此数量为剩余的活动数量。
	 */
	@ApiModelProperty(value = "剩余售空数量")
	private Integer soldOutCount;
}
