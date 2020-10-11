package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;


/**
 * <p>
 * 发现好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@ApiModel
public class EsFindGoodsForm implements Serializable {

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
	@NotBlank(message = "商品链接不能为空")
	private String goodsUrl;

	/**
	 * 图片地址
	 */
	@ApiModelProperty(value = "图片地址")
	private String picUrl;

	@ApiModelProperty(value = "相册")
	private List<EsFindGoodsGalleryForm> galleryList;

	/**
	 * 商品描述
	 */
	@ApiModelProperty(value = "商品描述")
	private String goodsDescription;

	}
