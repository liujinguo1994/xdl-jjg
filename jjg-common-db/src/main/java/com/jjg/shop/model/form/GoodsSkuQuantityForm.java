package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@Api
public class GoodsSkuQuantityForm implements Serializable {
    /**
     * 商品ID
     */
    @ApiModelProperty(value = "goodsId")
    private Long goodsId;

    /**
     * SKUID
     */
    @ApiModelProperty(value = "SKUID")
    private Long skuId;
    /**
     * 商品数量
     */
    @ApiModelProperty(value = "商品数量")
    private Integer goodsNumber;
}
