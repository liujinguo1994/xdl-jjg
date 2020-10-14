package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@Api
public class EsPromotionGoodsForm implements Serializable {

    /**
     * 商品id
     */
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;
    /**
     * Sku ID
     */
    @ApiModelProperty(value = "SkuID")
    private Long skuId;
    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间")
    private Long createTime;
    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间")
    private Long updateTime;
    /**
     * 活动标题
     */
    @ApiModelProperty(value = "活动标题")
    private String title;
    /**
     * 参与活动的商品数量
     */
    @ApiModelProperty(value = " 参与活动的商品数量")
    private Integer num;
    /**
     * 活动时商品的价格
     */
    @ApiModelProperty(value = "活动时商品的价格")
    private Double price;

}
