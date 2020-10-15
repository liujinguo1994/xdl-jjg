package com.jjg.member.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@ApiModel
public class EsWapMemberReceiptForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 发票id
     */
	@ApiModelProperty(required = false,value = "主键id",example = "1")
	private Long id;

    /**
     * 会员id
     */
	@ApiModelProperty(required = false,value = "会员id",example = "1")
	private Long memberId;

    /**
     * 发票类型
     */
	@ApiModelProperty(required = true,value = "发票类型2普票，1专票")
	private String receiptType;

    /**
     * 发票抬头
     */
	@ApiModelProperty(required = true,value = "发票抬头")
	private String receiptTitle;

    /**
     * 发票内容
     */
	@ApiModelProperty(required = false,value = "发票内容")
	private String receiptContent;

    /**
     * 发票税号
     */
	@ApiModelProperty(required = false,value = "发票税号")
	private String taxNo;

    /**
     * 注册地址
     */
	@ApiModelProperty(required = false,value = "注册地址")
	private String regAddr;

    /**
     * 注册电话
     */
	@ApiModelProperty(required = true,value = "手机号")
	private String regTel;

    /**
     * 开户银行
     */
	@ApiModelProperty(required = false,value = "开户银行")
	private String bankName;

    /**
     * 银行账户
     */
	@ApiModelProperty(required = false,value = "银行账户")
	private String bankAccount;
	/**
	 * 邮箱
	 */
	@ApiModelProperty(required = true,value = "邮箱")
	private String email;
	/**
	 * 发票明细
	 */
	@ApiModelProperty(required = true,value = "发票明细1商品明细，2商品类别")
	private String invoiceDetails;

    /**
     * 是否为默认
     */
	@ApiModelProperty(required = true,value = "是否为默认1默认，2不默认")
	private Integer isDefault;

	/**
	 * 子订单号
	 */
	@ApiModelProperty(required = true,value = "子订单号")
	private String orderSn;

	/**
	 * 商品id
	 */
	@ApiModelProperty(required = false,value = "商品id")
	private Long goodsId;
	/**
	 * 订单商品明细主键
	 */
	@ApiModelProperty(required = true,value = "订单商品明细主键")
	private Long ItemsId;
	/**
	 * 社会信用统一代码
	 */
	@ApiModelProperty(required = false,value = "社会信用统一代码")
	private String creditCode;
	/**
	 * 单位名称
	 */
	@ApiModelProperty(required = false,value = "单位名称")
	private String department;
	/**
	 * 发票明细
	 */
	@ApiModelProperty(required = true,value = "1商品明细，2商品类别")
	private Integer receiptDetails;
	/**
	 * 企业名称
	 */
	@ApiModelProperty(required = false,value = "企业名称")
	private String company;
	/**
	 * 店铺id
	 */
	@ApiModelProperty(required = true,value = "店铺id")
	private Long shopId;
}
