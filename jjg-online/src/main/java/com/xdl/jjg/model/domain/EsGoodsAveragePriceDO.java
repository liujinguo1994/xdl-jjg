package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品客单价统计
 * <br/>
 * @author KThirty
 * @since 2020/5/26 15:27
 */
@Data
public class EsGoodsAveragePriceDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String goodsName; // 商品名称
    private Long categoryId; // 分类id
    private String categoryName; // 分类名称
    private String specJson;// 规格
    private Integer salesNum; // 销售数量
    private Double averagePrice;// 客单价
}
