package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  移动端-支付参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-20
 */
@Data
@ApiModel
public class EsPayForm implements Serializable {

    private static final long serialVersionUID = -9059584131740941296L;

    @ApiModelProperty(value = "单号，订单号或者交易号", required = true)
    @NotBlank(message = "单号不能为空")
    private String sn;

    @ApiModelProperty(value = "支付插件id",required = true)
    @NotBlank(message = "支付插件id不能为空")
    private String paymentPluginId;

    @ApiModelProperty(value = "支付模式：正常normal， 二维码 ar", required = true)
    @NotBlank(message = "支付模式不能为空")
    private String payMode;

    @ApiModelProperty(value = "调用客户端PC,WAP,APP,APPLET",  required = true)
    @NotBlank(message = "调用客户端不能为空")
    private String clientType;

    @ApiModelProperty(value = "交易类型trade，order", required = true)
    @NotBlank(message = "交易类型不能为空")
    private String tradeType;
}
