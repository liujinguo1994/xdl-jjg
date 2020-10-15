package com.jjg.trade.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@ApiModel(description = "历史发票信息")
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberReceiptHistoryVO implements Serializable {


    @ApiModelProperty(value = "发票id")
	private Long historyId;
    /**
     * 订单编号
     */
    @ApiModelProperty(value = "订单编号")
	private String orderSn;
    /**
     * 卖家id
     */
    @ApiModelProperty(value = "卖家id")
	private Long shopId;
    /**
     * 会员id
     */
    @ApiModelProperty(value = "会员id")
	private Long memberId;
    /**
     * 发票类型
     */
    @ApiModelProperty(value = "发票类型")
	private String receiptType;
    /**
     * 发票抬头
     */
    @ApiModelProperty(value = "发票抬头")
	private String receiptTitle;
    /**
     * 发票金额
     */
    @ApiModelProperty(value = "发票金额")
	private Double receiptAmount;
    /**
     * 发票内容
     */
    @ApiModelProperty(value = "发票内容")
	private String receiptContent;
    /**
     * 税号
     */
    @ApiModelProperty(value = "税号")
	private String taxNo;
    /**
     * 注册地址
     */
    @ApiModelProperty(value = "注册地址")
	private String regAddr;
    /**
     * 注册电话
     */
    @ApiModelProperty(value = "注册电话")
	private String regTel;
    /**
     * 开户银行
     */
    @ApiModelProperty(value = "开户银行")
	private String bankName;
    /**
     * 银行账户
     */
    @ApiModelProperty(value = "银行账户")
	private String bankAccount;
    /**
     * 开票时间
     */
    @ApiModelProperty(value = "开票时间")
	private Long createTime;
    /**
     * 会员名称
     */
    @ApiModelProperty(value = "会员名称")
	private String memberName;
    /**
     * 开票状态
     */
    @ApiModelProperty(value = "开票状态")
	private Integer state;
    /**
     * 失败原因
     */
    @ApiModelProperty(value = "失败原因")
	private String failReason;

    /**
     * 商品id
     */
    @ApiModelProperty(value = "失败原因")
    private Long goodsId;
    /**
     * 发票图片路径
     */
    @ApiModelProperty(value = "发票图片路径")
    private String invoiceFileUrl;
    /**
     * 发票图片
     */
    @ApiModelProperty(value = "发票图片")
    private String invoiceImageUrl;
    /**
     * 发票流水号
     */
    @ApiModelProperty(value = "发票流水号")
    private String invoiceSerialNum;
    /**
     * 发票代码
     */
    @ApiModelProperty(value = "发票代码")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ApiModelProperty(value = "发票号码")
    private String invoiceNum;



}
