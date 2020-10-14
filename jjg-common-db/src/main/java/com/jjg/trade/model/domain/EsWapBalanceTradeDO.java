package com.xdl.jjg.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class EsWapBalanceTradeDO implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 订单编号
     */
	private String tradeSn;

	/**
	 * 订单编号
	 */
	private String orderSn;

    /**
     * 支付方式名称
     */
	private String paymentMethodName;

    /**
     * 支付插件名称
     */
	private String pluginId;
    /**
     * 总价格
     */
	private Double totalMoney;

	/**
	 * 余额支付
	 */
	private Double useBalance;

    /**
     * 订单状态
     */
	private String tradeStatus;


	/**
	 * 支付金额
	 */
	private Double needMoney;





}
