package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

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
	 * 商品id
	 */
	@ApiModelProperty(value = "SkuId")
	private Long skuId;
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
     * 参与活动的商品数量
     */
	@ApiModelProperty(value = "参与活动的商品数量")
	private Integer num;
    /**
     * 活动时商品的价格
     */
	@ApiModelProperty(value = "活动时商品的价格")
	private Double price;
    /**
     * 商家ID
     */
	@ApiModelProperty(value = "商家ID")
	private Long shopId;

	/**
	 * 是否下架 0.没有下架，1 下架
	 */
	@ApiModelProperty(value = "活动是否下架 0.没有下架，1 下架")
	private Integer isLowerShelf;

	protected Serializable pkVal() {
		return this.id;
	}

}
