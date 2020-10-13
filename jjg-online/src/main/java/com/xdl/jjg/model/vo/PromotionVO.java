package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 商品活动对照VO
 * </p>
 *
 * @author libw 981087977@qq.com
 * @Date: 6/11/2019 15:37
 */
@Data
public class PromotionVO implements Serializable {

	private static final long serialVersionUID = 4796645552318671313L;

	@ApiModelProperty(value = "商品id")
	private Long goodsId;

	@ApiModelProperty(value = "商品图片")
	private String thumbnail;

	@ApiModelProperty(value = "商品名称")
	private String name;

	@ApiModelProperty(value = "货品id")
	private Long skuId;

	@ApiModelProperty(value = "活动开始时间")
	private Long startTime;

	@ApiModelProperty(value = "活动结束时间")
	private Long endTime;

	@ApiModelProperty(value = "活动id")
	private Long activityId;

	/**
	 * 由此字段识别具体的活动类型
	 * 此字段对应的是一个枚举值
	 * @see
	 */
	@ApiModelProperty(value = "活动工具类型")
	private String promotionType;

	@ApiModelProperty(value = "活动名称")
	private String title;

	@ApiModelProperty(value = "满优惠活动")
	private EsFullDiscountVO fullDiscount;

	@ApiModelProperty(value = "满赠的赠品VO")
	private EsFullDiscountGiftVO fullDiscountGift;

	@ApiModelProperty(value = "单品立减活动")
	private EsMinusVO minus;

	@ApiModelProperty(value = "商品折扣活动")
	private EsGoodsDiscountVO goodsDiscount;

	@ApiModelProperty(value = "第二件半价活动")
	private EsHalfPriceVO halfPrice;

	@ApiModelProperty(value = "限时抢购活动")
	private SeckillGoodsVO seckillGoods;

	@ApiModelProperty(value = "售空数量")
	private Integer num;

	@ApiModelProperty(value = "活动价格")
	private Double price;

	@ApiModelProperty(value = "公司折扣")
	private Double discount;

	/**
	 * 公司标识符
	 */
	private String companyCode;

	/**
	 * 商品折扣
	 */
	@ApiModelProperty(value = "商品折扣")
	private Double goodsDiscounts;
}
