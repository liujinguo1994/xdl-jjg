package com.xdl.jjg.model.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发现好货相册
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-15
 */
@Data
@ApiModel
public class EsFindGoodsGalleryVO implements Serializable {

private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;

	/**
	 * 发现好货ID
	 */
	@ApiModelProperty(value = "发现好货ID")
	private Long findGoodsId;

	/**
	 * 图片路径
	 */
	@ApiModelProperty(value = "图片路径")
	private String url;

	}
