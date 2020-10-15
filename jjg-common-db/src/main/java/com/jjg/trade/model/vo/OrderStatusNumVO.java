package com.jjg.trade.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单各个状态的订单数
 * <br/>所有状态查看 OrderTagEnum
 *
 * @author Snow create in 2018/6/14
 * @version v2.0
 * @since v7.0.0
 */
@Data
public class OrderStatusNumVO implements Serializable {


    @ApiModelProperty(value = "所有订单数")
    private Integer allNum;

    @ApiModelProperty(value = "待付款订单数")
    private Integer waitPayNum;

    @ApiModelProperty(value = "待发货订单数")
    private Integer waitShipNum;

    @ApiModelProperty(value = "待收货订单数")
    private Integer waitRogNum;

    @ApiModelProperty(value = "已取消订单数")
    private Integer cancelNum;

    @ApiModelProperty(value = "已完成订单数")
    private Integer completeNum;

    @ApiModelProperty(value = "待评论订单数")
    private Integer waitCommentNum;

    @ApiModelProperty(value = "售后中订单数")
    private Integer refundNum;

}
