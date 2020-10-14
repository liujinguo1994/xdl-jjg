package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsGoodsQuantityDO implements Serializable {
    //商品ID
    private Long goodsId;
    //SKU ID
    private Long skuId;
    //实际库存
    private Integer quantity;
    //可用库存
    private Integer enableQuantity;
}
