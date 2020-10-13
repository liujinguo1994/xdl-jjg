package com.xdl.jjg.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsDraftGoodsQueryDTO implements Serializable {


	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类路径")
	private String categoryPath;
	private Integer isVirtual;
}
