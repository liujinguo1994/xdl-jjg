package com.xdl.jjg.model.dto;

import lombok.Data;

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
public class EsGoodsSkuQueryDTO implements Serializable {


	/**
	 * 商品编号\商品名称
	 */
	private String keyword;

	/**
	 * 商品分类路径
	 */
	private String categoryPath;
	/**
	 * 商品主键ID
	 */
	private Long goodsId;

	private Long isEnable;

	private Long shopId;
}
