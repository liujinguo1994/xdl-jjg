package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 会员支付帐单-es_payment_billQueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsPaymentBillQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;

    /**
     * 提交给第三方平台单号
     */
	@ApiModelProperty(value = "提交给第三方平台单号")
	private String outTradeNo;

    /**
     * 第三方平台返回交易号
     */
	@ApiModelProperty(value = "第三方平台返回交易号")
	private String returnTradeNo;

    /**
     * 是否已支付
     */
	@ApiModelProperty(value = "是否已支付")
	private Integer isPay;

    /**
     * 交易类型(交易；订单)
     */
	@ApiModelProperty(value = "交易类型(交易；订单)")
	private String tradeType;

    /**
     * 支付方式名称
     */
	@ApiModelProperty(value = "支付方式名称")
	private String paymentName;

    /**
     * 支付参数(第三方配置信息)
     */
	@ApiModelProperty(value = "支付参数(第三方配置信息)")
	private String payConfig;

    /**
     * 交易金额
     */
	@ApiModelProperty(value = "交易金额")
	private Double tradeMoney;

    /**
     * 支付插件id
     */
	@ApiModelProperty(value = "支付插件id")
	private String paymentPluginId;

}
