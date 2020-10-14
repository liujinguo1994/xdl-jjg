package com.jjg.member.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-热销商品DTO
 */
@Data
public class EsGoodsHotSellDTO implements Serializable {
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
