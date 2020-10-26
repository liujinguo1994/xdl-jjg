package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * 货运单
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@ApiModel(description = "货运单")
@Data
public class EsDeliveryVO implements Serializable {

    @ApiModelProperty(value = "订单编号" )
    private String orderSn;

    @ApiModelProperty(value = "货运单号" )
    private String deliveryNo;

    @ApiModelProperty(value = "物流公司" )
    private Integer logiId;

    @ApiModelProperty(value = "物流公司名称" )
    private String logiName;

    @ApiModelProperty(hidden=true )
    private String operator;

    @ApiModelProperty(value = "发货的商品id" )
    private List<Long> goodsId;



}
