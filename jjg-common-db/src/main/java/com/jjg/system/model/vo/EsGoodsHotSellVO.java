package com.jjg.system.model.vo;

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
@ApiModel(value = "管理端商品热卖榜")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsGoodsHotSellVO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "分类ID", example = "0")
    private Long categoryId;
    @ApiModelProperty(value = "分类名称")
    private String categoryName;
    @ApiModelProperty(value = "商品规格")
    private String specJson;
    @ApiModelProperty(value = "所属店铺")
    private String shopName;
    @ApiModelProperty(value = "总销售量", example = "0")
    private Integer salesNum;
    @ApiModelProperty(value = "总销售金额", example = "0")
    private Double totalMoney;

    @ApiModelProperty(value = "商品规格文本", example = "0")
    public String getSpecText() {
        return StringUtils.isNotBlank(this.specJson) ? JSONArray.parseArray(this.specJson).stream().map(o -> JSONObject.parseObject(o.toString()).getString("specValue")).collect(Collectors.joining(" * ")) : "";
    }
}
