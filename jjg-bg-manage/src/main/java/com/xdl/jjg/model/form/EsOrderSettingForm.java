package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 订单设置form
 */
@Data
@ApiModel
public class EsOrderSettingForm implements Serializable {

    private static final long serialVersionUID = -1549672177047538309L;

    @ApiModelProperty(required = true, value = "显示几天内订单天数", example = "1")
    @NotNull(message = "显示几天内订单天数不能为空")
    private Integer showOrderDay;

    @ApiModelProperty(required = true, value = "待支付订单自动关闭时间", example = "1")
    @NotNull(message = "待支付订单自动关闭时间不能为空")
    private Integer closeOrderDay;

    @ApiModelProperty(required = true, value = "发货几天自动完成订单", example = "1")
    @NotNull(message = "发货几天自动完成订单不能为空")
    private Integer finishOrderDay;

    @ApiModelProperty(required = true, value = "完成订单几天内可以退货", example = "1")
    @NotNull(message = "完成订单几天内可以退货不能为空")
    private Integer returnedGoodsDay;

    @ApiModelProperty(required = true, value = "完成订单几天内可以换货", example = "1")
    @NotNull(message = "完成订单几天内可以换货不能为空")
    private Integer exchangeGoodsDay;

    @ApiModelProperty(required = true, value = "订单发票税率", example = "1")
    @NotNull(message = "订单发票税率不能为空")
    private Double invoiceTaxRate;

    @ApiModelProperty(required = true, value = "自动确认收货天数", example = "1")
    @NotNull(message = "自动确认收货天数不能为空")
    private Integer autoReceivingDay;

}
