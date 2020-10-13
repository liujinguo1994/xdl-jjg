package com.xdl.jjg.model.vo;

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

    @ApiModelProperty(value = "显示几天内订单天数",example = "1")
    private Integer showOrderDay;

    @ApiModelProperty(value = "待支付订单自动关闭时间",example = "1")
    private Integer closeOrderDay;

    @ApiModelProperty(value = "发货几天自动完成订单",example = "1")
    private Integer finishOrderDay;

    @ApiModelProperty(value = "完成订单几天内可以退货",example = "1")
    private Integer returnedGoodsDay;

    @ApiModelProperty(value = "完成订单几天内可以换货",example = "1")
    private Integer exchangeGoodsDay;

    @ApiModelProperty(value = "订单发票税率",example = "1")
    private Double invoiceTaxRate;

}
