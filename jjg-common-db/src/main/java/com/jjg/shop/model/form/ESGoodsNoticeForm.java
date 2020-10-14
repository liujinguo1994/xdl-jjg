package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel
public class ESGoodsNoticeForm implements Serializable {

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "商品信息")
    private String goodsInfo;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = " 商品类型")
    private String categoryName;

    /**
     * 商品信息
     */
    @ApiModelProperty(value = "价格")
    private Double price;
}
