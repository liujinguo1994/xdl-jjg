package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品销售明细
 */
@Data
public class EsGoodsSalesDetailDO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    private Long goodsId;
    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 下单量
     */
    private Integer orderNum;
    /**
     * 下单商品总数
     */
    private Integer orderGoodsNum;
    /**
     * 下单价格总和
     */
    private Double orderPriceTotal;
}
