package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 热门榜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@ApiModel
public class EsGoodsRankingVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 榜单名称
	 */
	@ApiModelProperty(value = "榜单名称")
	private String rankingName;

	/**
	 * 推荐商品链接
	 */
	@ApiModelProperty(value = "goodsUrl")
	private String goodsUrl;

	/**
	 * 推荐商品id
	 */
	@ApiModelProperty(value = "推荐商品id")
	private Long goodsId;

	/**
	 * 商品分类id
	 */
	@ApiModelProperty(value = "商品分类id")
	private Long goodsCategoryId;

	/**
	 * 榜单图片地址
	 */
	@ApiModelProperty(value = "榜单图片地址")
	private String rankingPicUrl;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private Long createTime;

	/**
	 * 最后修改时间
	 */
	@ApiModelProperty(value = "最后修改时间")
	private Long updateTime;

	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double money;

	/**
	 * 购买数量
	 */
	@ApiModelProperty("购买数量")
	private Integer buyCount;

	/**
	 * 商品名称
	 */
	@ApiModelProperty("商品名称")
	private String goodsName;

	/**
	 * 商品原图
	 */
	@ApiModelProperty("商品原图")
	private String original;

	/**
	 * 是否放首页（1是，2否）
	 */
	@ApiModelProperty(value = "是否放首页（1是，2否）")
	private Integer homePage;

	/**
	 * 备注(首页展示时的榜单名称)
	 */
	@ApiModelProperty(value = "备注(首页展示时的榜单名称)")
	private String remark;

	/**
	 * 背景图片地址
	 */
	@ApiModelProperty(value = "背景图片地址")
	private String backgroundPicUrl;


	@ApiModelProperty(value = "榜单销量")
	private Integer count;

}
