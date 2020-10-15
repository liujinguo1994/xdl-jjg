package com.jjg.trade.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品销售转换率
 * <br/>
 * @author KThirty
 * @since 2020/5/26 15:27
 */
@Data
public class EsGoodsPaymentConversionRateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 分类名称
     */
    private String categoryName;
    /**
     * 店铺名称
     */
    private String shopName;
    /**
     * 销售时间开始
     */
    private Long saleTimeStart;
    /**
     * 销售时间结束
     */
    private Long saleTimeEnd;
}
