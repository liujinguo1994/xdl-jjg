package com.jjg.shop.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsGoodsSkuQuantityDO implements Serializable {

    /**
     * 商品ID
     */
    private Long goodsId;

    /**
     * SKUID
     */
    private Long skuId;
    /**
     * 库存
     */
    private Integer quantity;
    /**
     * 可用库存
     */
    private Integer enableQuantity;
}
