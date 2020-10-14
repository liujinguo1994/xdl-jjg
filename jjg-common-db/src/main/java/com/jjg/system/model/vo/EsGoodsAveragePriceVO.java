package com.jjg.member.model.vo;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.stream.Collectors;

@Data
@ApiModel(value = "管理端商品客单价排行")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsAveragePriceVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "分类ID", example = "0")
    private Long categoryId;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "商品规格")
    private String specJson;
    @ApiModelProperty(value = "总销售量", example = "0")
    private Integer salesNum;
    @ApiModelProperty(value = "客单价", example = "0")
    private Double averagePrice;

    @ApiModelProperty(value = "商品规格文本", example = "0")
    public String getSpecText() {
        return StringUtils.isNotBlank(this.getSpecJson()) ? JSONArray.parseArray(this.getSpecJson()).stream().map(o -> JSONObject.parseObject(o.toString()).getString("specValue")).collect(Collectors.joining(" * ")) : "";
    }
}
