package com.xdl.jjg.model.dto;

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
public class EsSeckillApplyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 活动id
     */
	private Long seckillId;
    /**
     * 时刻
     */
	private Integer timeLine;
	/**
	 * 活动开始日期
	 */
	private Long startDay;
	/**
	 * 活动结束日期
	 */
	private Long endTime;
    /**
     * 商品ID
     */
	private Long goodsId;
    /**
     * 商品名称
     */
	private String goodsName;
    /**
     * 商家ID
     */
	private Long shopId;
    /**
     * 商家名称
     */
	private String shopName;
    /**
     * 价格
     */
	private Double money;
    /**
     * 售空数量
     */
	private Integer soldQuantity;
    /**
     * 申请状态
     */
	private Integer state;
    /**
     * 驳回原因
     */
	private String failReason;
    /**
     * 商品原始价格
     */
	private Double originalPrice;
    /**
     * 已售数量
     */
	private Integer salesNum;

	/**
	 * 图片
	 */
	private String image;

	/**
	 * 商品SKU ID
	 */
	private Long skuId;

	/**
	 * SKU规格
	 */
	private String specs;

	/**
	 * SKU编号
	 */
	private String skuSn;

	protected Serializable pkVal() {
		return this.id;
	}
}
