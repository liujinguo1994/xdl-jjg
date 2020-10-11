package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 常买商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsOftenGoodsVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@ApiModelProperty(value = "主键id")
	private Long id;

	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;

	/**
	 * 自定义分类id
	 */
	@ApiModelProperty(value = "自定义分类id")
	private Long customCategoryId;

	/**
	 * 商品链接
	 */
	@ApiModelProperty(value = "商品链接")
	private String goodsUrl;

	/**
	 * 图片地址
	 */
	@ApiModelProperty(value = "图片地址")
	private String picUrl;

	/**
	 * 商品id
	 */
	@ApiModelProperty(value = "商品id")
	private Long goodsId;

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
	 * 自定义分类名称
	 */
	@ApiModelProperty(value = "自定义分类名称")
	private String categoryName;

	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	private Double money;

	}
