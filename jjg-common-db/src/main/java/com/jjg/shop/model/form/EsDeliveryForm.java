package com.xdl.jjg.model.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * 货运单
 * @author Snow create in 2018/5/15
 * @version v2.0
 * @since v7.0.0
 */
@Api
@Data
public class EsDeliveryForm implements Serializable {

    @ApiModelProperty(value = "订单编号" )
    @NotBlank(message = "订单编号不能为空")
    private String orderSn;

    @ApiModelProperty(value = "货运单号" )
    @NotBlank(message = "货运单号不能为空")
    private String deliveryNo;

    @ApiModelProperty(value = "物流公司" )
    private Long logiId;

    @ApiModelProperty(value = "物流公司名称" )
    @NotBlank(message = "物流公司名称不能为空")
    private String logiName;

    @ApiModelProperty(value = "发货的商品id" )

    private List<Integer> goodsId;



}
