package com.xdl.jjg.model.enums;

/**
 * auth wangaf
 */
public enum GoodsCachePrefix {
    /**
     * 商品
     */
    GOODS,

    /**
     * 商品sku
     */
    SKU,

    /**
     * 商品分类
     */
    GOODS_CAT,
    /**
     * 商品订单 供库存扣减的时候使用
     */
    GOODS_ORDER;


    public String getPrefix() {
        return this.name() + "_";
    }
}
