package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 商品总量统计
 * <br/>
 * @author KThirty
 * @since 2020/5/27 14:33
 */
@Data
public class EsGoodsTotalStatisticsDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品总数
     */
    private Integer goodsCount;
    /**
     * 自营商品总数
     */
    private Integer selfOperatedCount;
    /**
     * 上架商品总数
     */
    private Integer shelvesCount;
    /**
     * 下架商品总数
     */
    private Integer unShelvesCount;
    /**
     * 商品SKU总数
     */
    private Integer skuCount;
    /**
     * 品牌总数
     */
    private Integer brandCount;
}
