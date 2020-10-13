package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-05-31
 */
@Data
@Accessors(chain = true)
public class EsGroupBuyGoodsDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * SKUId
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long skuId;

    /**
     * 活动Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long actId;

    /**
     * 团购分类
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long catId;

    /**
     * 地区Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long areaId;

    /**
     * 团购名称
     */
	private String gbName;

    /**
     * 副标题
     */
	private String gbTitle;

    /**
     * 商品名称
     */
	private String goodsName;

    /**
     * 商品Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsId;

    /**
     * 原始价格
     */
	private Double originalPrice;

    /**
     * 团购价格
     */
	private Double price;

    /**
     * 图片地址
     */
	private String imgUrl;

    /**
     * 商品总数
     */
	private Integer goodsNum;

    /**
     * 虚拟数量
     */
	private Integer visualNum;

    /**
     * 限购数量
     */
	private Integer limitNum;

    /**
     * 已团购数量
     */
	private Integer buyNum;

    /**
     * 浏览数量
     */
	private Integer viewNum;

    /**
     * 介绍
     */
	private String remark;

    /**
     * 状态
     */
	private Integer gbStatus;

    /**
     * 添加时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long addTime;

    /**
     * wap缩略图
     */
	private String wapThumbnail;

    /**
     * pc缩略图
     */
	private String thumbnail;

    /**
     * 商家ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long sellerId;

    /**
     * 店铺名称
     */
	private String sellerName;

	protected Serializable pkVal() {
		return this.id;
	}

}
