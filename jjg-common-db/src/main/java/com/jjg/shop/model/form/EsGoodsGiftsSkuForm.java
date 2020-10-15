package com.jjg.shop.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsGoodsGiftsSkuForm implements Serializable {
	/**
	 * SKU ID
	 */
	@ApiModelProperty(value = "商品SKU ID")
	private Long id;
	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用 1 启用 2不启用 默认1")
	private Boolean isEnable;

	@ApiModelProperty(value = "商品条码")
	private String barCode;
	@ApiModelProperty(value = "实际库存")
	private Integer quantity;

	@ApiModelProperty(value = "虚拟库存")
	private Integer xnQuantity;

}
