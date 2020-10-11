package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

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
public class EsGoodsRankingForm implements Serializable {

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
	@ApiModelProperty(value = "推荐商品链接")
	@NotBlank(message = "推荐商品链接不能为空")
	private String goodsUrl;

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
	 * 是否放首页（1是，2否）
	 */
	@ApiModelProperty(value = "是否放首页（1是，2否）")
	private Integer homePage;

	/**
	 * 备注
	 */
	@ApiModelProperty(value = "备注")
	private String remark;

	/**
	 * 背景图片地址
	 */
	@ApiModelProperty(value = "背景图片地址")
	private String backgroundPicUrl;

}
