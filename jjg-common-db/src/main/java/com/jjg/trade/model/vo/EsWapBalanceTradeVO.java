package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 余额支付
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-23
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsWapBalanceTradeVO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单编号
     */
	@ApiModelProperty(required = false,value = "交易编号")
	private String tradeSn;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(required = false,value = "订单编号")
	private String orderSn;

    /**
     * 支付方式名称
     */
	@ApiModelProperty(required = false,value = "支付方式名称")
	private String paymentMethodName;

    /**
     * 支付插件名称
     */
	@ApiModelProperty(required = false,value = "支付插件名称")
	private String pluginId;
    /**
     * 总价格
     */
	@ApiModelProperty(required = false,value = "总价格")
	private Double totalMoney;

	/**
	 * 余额支付
	 */
	@ApiModelProperty(required = false,value = "余额支付（如果使用余额小于交易总额，则不对订单进行余额付款）")
	private Double useBalance;

    /**
     * 订单状态
     */
	@ApiModelProperty(required = false,value = "订单状态")
	private String tradeStatus;


	/**
	 * 第三方支付金额
	 */
	@ApiModelProperty(required = false,value = "第三方支付金额")
	private Double needMoney;





}
