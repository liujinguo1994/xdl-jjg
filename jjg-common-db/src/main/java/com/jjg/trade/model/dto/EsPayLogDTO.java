package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 支付记录日志表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsPayLogDTO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 支付类型(支付宝、微信、银联)
     */
	private String payType;
    /**
     * 付款时间
     */
	private Long payTime;
    /**
     * 付款金额
     */
	private Double payMoney;
    /**
     * 付款会员名
     */
	private String payMemberName;
    /**
     * 付款状态
     */
	private String payState;
    /**
     * 收款单流水号
     */
	private String payLogSn;


	protected Serializable pkVal() {
		return this.id;
	}

}
