package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsGroupBuyGoodsDO extends Model<EsGroupBuyGoodsDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * SKUId
     */
	private Long skuId;

    /**
     * 活动Id
     */
	private Long actId;

    /**
     * 团购分类
     */
	private Long catId;

    /**
     * 地区Id
     */
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
	private Long sellerId;

    /**
     * 店铺名称
     */
	private String sellerName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
