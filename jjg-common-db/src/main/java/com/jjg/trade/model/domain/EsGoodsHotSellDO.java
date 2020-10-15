package com.jjg.trade.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品热卖榜
 * <br/>
 * @author KThirty
 * @since 2020/5/26 15:27
 */
@Data
public class EsGoodsHotSellDO implements Serializable {
    private static final long serialVersionUID = 1L;
    private String goodsName;
    private Long categoryId;
    private String categoryName;
    private String specJson;
    private String shopName;
    private Integer salesNum;
    private Double totalMoney;
}
