package com.jjg.shop.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Api
@Data
public class EsFullDiscountGiftForm implements Serializable {

    @ApiModelProperty(value = "主键ID")
    private Long id;

    @ApiModelProperty(value = "赠品名称")
    private String giftName;

    @ApiModelProperty(value = "赠品价格")
    private Double giftMoney;

    @ApiModelProperty(value = "赠品图片")
    private String giftImg;

    @ApiModelProperty(value = "赠品数量")
    private Integer enableStore;


    @ApiModelProperty(value = "实际库存")
    private Integer quantity;

    @ApiModelProperty(value = "虚拟库存片")
    private Integer xnQuantity;

    @ApiModelProperty(value = "商品SKU集合")
    @NotNull.List(value = {})
    private List<EsGoodsGiftsSkuForm> skuList;
}
