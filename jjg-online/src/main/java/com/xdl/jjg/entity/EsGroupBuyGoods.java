package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
@TableName("es_group_buy_goods")
public class EsGroupBuyGoods extends Model<EsGroupBuyGoods> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Double id;

	/**
	 * SKUId
	 */
	@TableField("sku_id")
	private Long skuId;

	/**
	 * 活动Id
	 */
	@TableField("act_id")
	private Long actId;

	/**
	 * 团购分类
	 */
	@TableField("cat_id")
	private Long catId;

	/**
	 * 地区Id
	 */
	@TableField("area_id")
	private Long areaId;

	/**
	 * 团购名称
	 */
	@TableField("gb_name")
	private String gbName;

	/**
	 * 副标题
	 */
	@TableField("gb_title")
	private String gbTitle;

	/**
	 * 商品名称
	 */
	@TableField("goods_name")
	private String goodsName;

	/**
	 * 商品Id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 原始价格
	 */
	@TableField("original_price")
	private Double originalPrice;

	/**
	 * 团购价格
	 */
	@TableField("price")
	private Double price;

	/**
	 * 图片地址
	 */
	@TableField("img_url")
	private String imgUrl;

	/**
	 * 商品总数
	 */
	@TableField("goods_num")
	private Integer goodsNum;

	/**
	 * 虚拟数量
	 */
	@TableField("visual_num")
	private Integer visualNum;

	/**
	 * 限购数量
	 */
	@TableField("limit_num")
	private Integer limitNum;

	/**
	 * 已团购数量
	 */
	@TableField("buy_num")
	private Integer buyNum;

	/**
	 * 浏览数量
	 */
	@TableField("view_num")
	private Integer viewNum;

	/**
	 * 介绍
	 */
	@TableField("remark")
	private String remark;

	/**
	 * 状态
	 */
	@TableField("gb_status")
	private Integer gbStatus;

	/**
	 * 添加时间
	 */
	@TableField("add_time")
	private Long addTime;

	/**
	 * wap缩略图
	 */
	@TableField("wap_thumbnail")
	private String wapThumbnail;

	/**
	 * pc缩略图
	 */
	@TableField("thumbnail")
	private String thumbnail;

	/**
	 * 商家ID
	 */
	@TableField("seller_id")
	private Long sellerId;

	/**
	 * 店铺名称
	 */
	@TableField("seller_name")
	private String sellerName;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
