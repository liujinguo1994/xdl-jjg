package com.jjg.trade.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsPromotionGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 商品id
     */
	private Long goodsId;
	/**
	 * SKU id
	 */
	private Long skuId;
    /**
     * 创建时间
     */
	private Long createTime;
    /**
     * 更新时间
     */
	private Long updateTime;
    /**
     * 活动id
     */
	private Long activityId;
    /**
     * 活动类型
     */
	private String promotionType;
    /**
     * 活动标题
     */
	private String title;
    /**
     * 参与活动的商品数量
     */
	private Integer num;
    /**
     * 活动时商品的价格
     */
	private Double price;
    /**
     * 商家ID
     */
	private Long shopId;

	/**
	 * 当前时间
	 */
	private Long currTime;

	/**
	 * 商品名称
	 */
	private String goodsName;

	/**
	 * 缩略图
	 */
	private String thumbnail;

	/**
	 * 活动开始时间
	 */
	private Long startTime;
	/**
	 * 活动结束时间
	 */
	private Long endTime;
	/**
	 * 是否下架 0.没有下架，1 下架
	 */
	private Integer isLowerShelf;

	protected Serializable pkVal() {
		return this.id;
	}

}
