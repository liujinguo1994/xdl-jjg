package com.jjg.trade.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发票历史
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsMemberReceiptHistoryDO implements Serializable {


    /**
     * 发票id
     */
	private Long historyId;
    /**
     * 订单编号
     */
	private String orderSn;
    /**
     * 卖家id
     */
	private Long shopId;
    /**
     * 会员id
     */
	private Long memberId;
    /**
     * 发票类型
     */
	private String receiptType;
    /**
     * 发票抬头
     */
	private String receiptTitle;
    /**
     * 发票金额
     */
	private Double receiptAmount;
    /**
     * 发票内容
     */
	private String receiptContent;
    /**
     * 税号
     */
	private String taxNo;
    /**
     * 注册地址
     */
	private String regAddr;
    /**
     * 注册电话
     */
	private String regTel;
    /**
     * 开户银行
     */
	private String bankName;
    /**
     * 银行账户
     */
	private String bankAccount;
    /**
     * 开票时间
     */
	private Long createTime;
    /**
     * 会员名称
     */
	private String memberName;
    /**
     * 开票状态
     */
	private Integer state;
    /**
     * 失败原因
     */
	private String failReason;



}
