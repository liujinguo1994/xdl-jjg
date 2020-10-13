package com.xdl.jjg.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品销售明细DTO
 */
@Data
public class EsGoodsSalesDetailDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 分类ID
     */
    private Long categoryId;
    /**
     * 店铺ID
     */
    private Long shopId;
    /**
     * 查询周期
     */
    private String queryCycle;
    /**
     * 查询周期单位（ YEAR按年 MONTH按月）
     */
    private String queryCycleUnit;
    /**
     * 订单状态
     */
    private String orderState;
}
