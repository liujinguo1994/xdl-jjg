package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员支付帐单-es_payment_bill
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
public class EsPaymentBillDO extends Model<EsPaymentBillDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;

    /**
     * 订单编号
     */
	private String orderSn;

    /**
     * 提交给第三方平台单号
     */
	private String outTradeNo;

    /**
     * 第三方平台返回交易号
     */
	private String returnTradeNo;

    /**
     * 是否已支付
     */
	private Integer isPay;

    /**
     * 交易类型(交易；订单)
     */
	private String tradeType;

    /**
     * 支付方式名称
     */
	private String paymentName;

    /**
     * 支付参数(第三方配置信息)
     */
	private String payConfig;

    /**
     * 交易金额
     */
	private Double tradeMoney;

    /**
     * 支付插件id
     */
	private String paymentPluginId;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

}
