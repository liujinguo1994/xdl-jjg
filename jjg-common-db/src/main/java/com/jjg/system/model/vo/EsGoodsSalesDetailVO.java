package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 管理端报表-商品统计-商品销售明细
 */
@Data
@ApiModel(value = "管理端商品销售明细")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsSalesDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "商品ID", example = "0")
    private Long goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    /**
     * 下单量
     */
    @ApiModelProperty(value = "下单量", example = "0")
    private Integer orderNum;
    /**
     * 下单商品总数
     */
    @ApiModelProperty(value = "下单商品总数", example = "0")
    private Integer orderGoodsNum;
    /**
     * 下单价格总和
     */
    @ApiModelProperty(value = "下单价格总和", example = "0")
    private Double orderPriceTotal;
}
