package com.jjg.member.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 会员发票信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberReceiptDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员发票id
     */
	private Long id;

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
     * 发票内容
     */
	private String receiptContent;

    /**
     * 发票税号
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
     * 是否为默认
     */
	private Integer isDefault;
	/**
	 * 社会信用统一代码
	 */
	private String creditCode;
	/**
	 * 单位名称
	 */
	private String department;
	/**
	 * 商品明细
	 */
	private Integer receiptDetails;


}
