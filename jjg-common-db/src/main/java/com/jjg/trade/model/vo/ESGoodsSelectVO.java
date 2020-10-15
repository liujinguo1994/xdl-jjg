package com.jjg.trade.model.vo;/**
 * @author wangaf
 * @date 2019/11/15 13:57
 **/

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 @Author wangaf 826988665@qq.com
 @Date 2019/11/15
 @Version V1.0
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Api
public class ESGoodsSelectVO implements Serializable {

    @ApiModelProperty(value = "商品ID")
    private Long goodsId;
    @ApiModelProperty(value = "SkuId")
    private Long skuId;
    @ApiModelProperty(value = "库存")
    private Integer quantity;
    @ApiModelProperty(value = "商品价格")
    private Double money;
    @ApiModelProperty(value = "商品名称")
    private String goodsName;
    @ApiModelProperty(value = "商品编号")
    private String goodsSn;
}
