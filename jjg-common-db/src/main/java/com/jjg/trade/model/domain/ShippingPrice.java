package com.jjg.trade.model.domain;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 运费价格
 */
@Data
public class ShippingPrice {
    @ApiModelProperty(value = "配送费" )
    private Double  freightPrice;

    @ApiModelProperty(value = "普通配送费" )
    private Double  commonFreightPrice;

    @ApiModelProperty(value = "生鲜配送费" )
    private Double  freshFreightPrice;
}
