package com.xdl.jjg.model.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

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
public class EsPromotionGoodsForm implements Serializable {

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
	 * SkuId
	 */
	private Long skuId;
    /**
     * 活动开始时间
     */
	private Long createTime;
    /**
     * 活动结束时间
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
	private BigDecimal price;
    /**
     * 商家ID
     */
	private Long shopId;


	protected Serializable pkVal() {
		return this.id;
	}

}
