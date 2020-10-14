package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 商品库存
 * @author Liujg
 *
 */
@Data
@ApiModel
public class EsGoodsSkuQuantityVO implements Serializable{

	private static final long serialVersionUID = -2657189345989798891L;

	private Long goodsId;

	private Long skuId;

	private Integer quantity;

}

