package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * <p>
 *  移动端-支付回调参数
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-06
 */
@Data
@ApiModel
public class EsWapCallBackForm implements Serializable {

    private static final long serialVersionUID = -9059584131740941296L;

    @ApiModelProperty(value = "交易类型trade，order", required = true)
    @NotBlank(message = "交易类型不能为空")
    private String tradeType;

    @ApiModelProperty(value = "支付插件id",required = true)
    @NotBlank(message = "支付插件id不能为空")
    private String paymentPluginId;


    @ApiModelProperty(value = "调用客户端PC,WAP,app",  required = true)
    @NotBlank(message = "调用客户端不能为空")
    private String clientType;


}
