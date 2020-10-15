package com.jjg.shop.model.vo;/**
 * @author wangaf
 * @date 2019/11/2 14:37
 **/

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/2
 @Version V1.0
 **/
@Data
@Api
public class EsBuyerTagGoodsVO implements Serializable {

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 商品编号
     */
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;

    /**
     * 商品价格
     */
    @ApiModelProperty(value = "商品价格")
    private Double money;

    /**
     * 可用库存=SKU之和
     */
    @ApiModelProperty(value = "库存")
    private Integer quantity;
    /**
     * 购买数量
     */
    @ApiModelProperty(value = "购买数量")
    private Integer buyCount;
    /**
     * 原图路径
     */
    @ApiModelProperty(value = "原图路径")
    private String original;
}
