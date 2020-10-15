package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
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
@Api
public class EsGoodsSkuForm implements Serializable {
	/**
	 * SKU ID
	 */
	@ApiModelProperty(value = "商品SKU ID")
	private Long id;
	/**
	 * 可用库存=真实库存+虚拟库存-冻结库存
	 */
	@ApiModelProperty(value = "商品条码")
	private String barCode;
	@ApiModelProperty(value = "实际库存")
	@NotNull
	private Integer quantity;

	@ApiModelProperty(value = "虚拟库存")
	private Integer xnQuantity;
	/**
	 * 商品价格
	 */
	@ApiModelProperty(value = "商品价格")
	@NotNull(message = "商品价格不能为空")
	private Double money;

	/**
	 * 是否启用
	 */
	@ApiModelProperty(value = "是否启用 1 启用 2不启用 默认1")
	private Boolean isEnable;

	@ApiModelProperty(value = "是否自提  true 是 false否 ")
	private Boolean isSelf;
	/**
	 * 成本价格
	 */
	@ApiModelProperty(value = "成本价格")
	private Double cost;
	/**
	 * 商品相册集合
	 */
	@ApiModelProperty(value = "商品相册")
	private EsGoodsGalleryForm goodsGallery;



}
