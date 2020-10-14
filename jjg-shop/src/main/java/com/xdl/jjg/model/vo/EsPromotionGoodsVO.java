package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.goods.api.model.domain.cache.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 活动表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsPromotionGoodsVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键id")
	private Long id;
    /**
     * 商品id
     */
	@ApiModelProperty(value = "商品id")
	private Long goodsId;
    /**
     * 活动开始时间
     */
	@ApiModelProperty(value = "活动开始时间")
	private Long createTime;
    /**
     * 活动结束时间
     */
	@ApiModelProperty(value = "活动结束时间")
	private Long updateTime;
    /**
     * 活动id
     */
	@ApiModelProperty(value = "活动id")
	private Long activityId;
    /**
     * 活动类型
     */
	@ApiModelProperty(value = "活动类型")
	private String promotionType;
    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

	/**
	 * 活动名称
	 */
	@ApiModelProperty(value = "名称")
	private String  name;
    /**
     * 参与活动的商品数量
     */
	@ApiModelProperty(value = "参与活动的商品数量")
	private Integer num;
    /**
     * 活动时商品的价格
     */
	@ApiModelProperty(value = "活动时商品的价格")
	private BigDecimal price;
    /**
     * 商家ID
     */
	@ApiModelProperty(value = "商家ID")
	private Long shopId;
	//单品立减 活动VO
	@ApiModelProperty(value = "单品立减 活动VO")
	private EsMinusCO esMinusCO;
	// 商品折扣活动VO
	@ApiModelProperty(value = "商品折扣活动VO")
	private EsGoodsDiscountCO esGoodsDiscountCO;
	// 满减满赠VO
	@ApiModelProperty(value = "满减满赠VO")
	private EsFullDiscountCO esFullDiscountCO;
	// 第二件半价活动VO
	@ApiModelProperty(value = "第二件半价活动VO")
	private EsHalfPriceCO esHalfPriceCO;
	// 秒杀商品
	@ApiModelProperty(value = "秒杀商品")
	private EsSeckillGoodsCO seckillGoodsCO;
	// 企业用户
	@ApiModelProperty(value = "企业用户折扣")
	private EsDiscountCO esDiscountCO;



	protected Serializable pkVal() {
		return this.id;
	}

}
