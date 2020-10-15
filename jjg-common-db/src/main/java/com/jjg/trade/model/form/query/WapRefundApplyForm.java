package com.jjg.trade.model.form.query;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 售后申请VO
 * @author yaunj
 * @version v7.0
 * @since v7.0 上午10:33 2020/04/02
 */
@Api
@Data
public class WapRefundApplyForm implements Serializable{
    private static final long serialVersionUID = 758087208773569549L;
    @ApiModelProperty(name = "orderSn", value = "订单编号",required = true)
    private String orderSn;

    @ApiModelProperty(value = "SKUID",required = false)
    private Integer skuId;
    @ApiModelProperty(name = "returnNum", value = "退货数量",required = false)
    private Integer returnNum;

    @ApiModelProperty(name = "refundReason", value = "退款原因",required = true)
    private String refundReason;

    @ApiModelProperty(name = "customerRemark", value = "客户备注",required = false)
    private String customerRemark;

    @ApiModelProperty(name = "refundType", value = "退款RETURN_MONEY/退款&退货RETURN_GOODS/换货CHANGE_GOODS/维修REPAIR_GOODS",required = true)
    private String refundType;

    @ApiModelProperty(name = "refuseType", value = "维权类型(取消订单 CANCEL_ORDER,申请售后 AFTER_SALE)",required = true)
    private String refuseType;

    @ApiModelProperty(name = "url", value = "图片路径")
    private String url;



}
