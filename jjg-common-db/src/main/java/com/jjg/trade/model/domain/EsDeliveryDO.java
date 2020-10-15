package com.jjg.trade.model.domain;

import io.swagger.annotations.ApiModel;
import lombok.Data;

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
public class EsDeliveryDO implements Serializable {

    private String orderSn;
    private String deliveryNo;

    private Integer logiId;

    private String logiName;

    private String operator;

    private List<Long> goodsId;



}
