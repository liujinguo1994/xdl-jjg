package com.jjg.shop.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsIndexVO implements Serializable {
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
}