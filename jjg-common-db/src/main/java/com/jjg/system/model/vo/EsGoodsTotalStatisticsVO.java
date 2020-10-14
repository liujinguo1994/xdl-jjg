package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "管理端商品总数统计")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsTotalStatisticsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品总数
     */
    @ApiModelProperty(value = "商品总数", example = "0")
    private Integer goodsCount;
    /**
     * 自营商品总数
     */
    @ApiModelProperty(value = "自营商品总数", example = "0")
    private Integer selfOperatedCount;
    /**
     * 上架商品总数
     */
    @ApiModelProperty(value = "上架商品总数", example = "0")
    private Integer shelvesCount;
    /**
     * 下架商品总数
     */
    @ApiModelProperty(value = "下架商品总数", example = "0")
    private Integer unShelvesCount;
    /**
     * 商品SKU总数
     */
    @ApiModelProperty(value = "商品SKU总数", example = "0")
    private Integer skuCount;
    /**
     * 品牌总数
     */
    @ApiModelProperty(value = "品牌总数", example = "0")
    private Integer brandCount;
}
