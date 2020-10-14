package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 订单设置
 */
@Data
@ApiModel
public class EsOrderSettingVO implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    @ApiModelProperty(value = "显示几天内订单天数")
    private Integer showOrderDay;

    @ApiModelProperty(value = "待支付订单自动关闭时间")
    private Integer closeOrderDay;

    @ApiModelProperty(value = "发货几天自动完成订单")
    private Integer finishOrderDay;

    @ApiModelProperty(value = "完成订单几天内可以退货")
    private Integer returnedGoodsDay;

    @ApiModelProperty(value = "完成订单几天内可以换货")
    private Integer exchangeGoodsDay;

    @ApiModelProperty(value = "订单发票税率")
    private Double invoiceTaxRate;

    @ApiModelProperty(value = "自动确认收货天数")
    private Integer autoReceivingDay;

}
