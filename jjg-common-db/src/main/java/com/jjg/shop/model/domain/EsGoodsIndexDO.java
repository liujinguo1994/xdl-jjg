package com.jjg.shop.model.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class EsGoodsIndexDO implements Serializable {
    /**商品id*/
    private Long goodsId;

    /**商品名称*/
    private String name;

    /**缩略图*/
    private String original;

    /**商品价格*/
    private Double money;

    /**购买数*/
    private Integer buyCount;

    /**评论数*/
    private Integer commentNum;

    /**
     * 商品好評率
     */
    private Double grade;

    /**
     * 卖家id
     */
    private Integer shopId;
    /**
     * 店铺名字
     */
    private String shopName;

    private Long brandId;
}