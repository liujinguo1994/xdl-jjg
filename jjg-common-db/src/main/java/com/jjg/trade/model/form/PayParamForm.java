package com.jjg.trade.model.form;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author fk
 * @version v2.0
 * @Description: 调用支付使用参数
 * @date 2018/4/1616:54
 * @since v7.0.0
 */
@Data
@ApiModel
@JsonIgnoreProperties(ignoreUnknown = true)
public class PayParamForm {

    @ApiModelProperty(value = "单号，订单号或者交易号", required = true)
    private String sn;

    @ApiModelProperty(value = "支付插件id", name = "payment_plugin_id", required = true)
    private String paymentPluginId;

    @ApiModelProperty(value = "支付模式，正常normal， 二维码 ar,枚举类型PaymentPatternEnum", name = "pay_mode", required = true)
    private String payMode;

    @ApiModelProperty(value = "调用客户端PC,WAP,NATIVE,REACT", name = "client_type", required = true, allowableValues = "PC,WAP,APP,APPLET")
    private String clientType;

    @ApiModelProperty(value = "交易类型，trade，order", name = "trade_type", required = true)
    private String tradeType;

    @ApiModelProperty(value = "用户唯一标识")
    private String openid;
}
