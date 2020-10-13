package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class EsReceiptHistoryVO implements Serializable {


    /**
     * 发票id
     */
    @ApiModelProperty(required = false, value = "发票id",example = "121")
    private Long historyId;
    /**
     * 订单编号
     */
    @ApiModelProperty(required = false, value = "订单编号",example = "121")
    private String orderSn;
    /**
     * 卖家id
     */
    @ApiModelProperty(required = false, value = "卖家id",example = "121")
    private Long shopId;
    /**
     * 会员id
     */
    @ApiModelProperty(required = false, value = "会员id",example = "121")
    private Long memberId;
    /**
     * 发票类型
     */
    @ApiModelProperty(required = false, value = "会员id",example = "121")
    private String receiptType;
    /**
     * 发票抬头
     */
    @ApiModelProperty(required = false, value = "发票抬头",example = "个人")
    private String receiptTitle;
    /**
     * 发票金额
     */
    @ApiModelProperty(required = false, value = "发票金额",example = "12.55")
    private Double receiptAmount;
    /**
     * 发票内容
     */
    @ApiModelProperty(required = false, value = "发票内容")
    private String receiptContent;
    /**
     * 税号
     */
    @ApiModelProperty(required = false, value = "税号")
    private String taxNo;
    /**
     * 注册地址
     */
    @ApiModelProperty(required = false, value = "注册地址")
    private String regAddr;
    /**
     * 注册电话
     */
    @ApiModelProperty(required = false, value = "注册电话")
    private String regTel;
    /**
     * 开户银行
     */
    @ApiModelProperty(required = false, value = "开户银行")
    private String bankName;
    /**
     * 银行账户
     */
    @ApiModelProperty(required = false, value = "银行账户")
    private String bankAccount;
    /**
     * 开票时间
     */
    @ApiModelProperty(required = false, value = "开票时间")
    private Long createTime;
    /**
     * 会员名称
     */
    @ApiModelProperty(required = false, value = "会员名称")
    private String memberName;
    /**
     * 开票状态
     */
    @ApiModelProperty(required = false, value = "开票状态")
    private Integer state;
    /**
     * 失败原因
     */
    @ApiModelProperty(required = false, value = "失败原因")
    private String failReason;
    /**
     * 商品id
     */
    @ApiModelProperty(required = false, value = "商品id")
    private Long goodsId;
    /**
     * 发票图片路径
     */
    @ApiModelProperty(required = false, value = "发票图片路径")
    private String invoiceFileUrl;
    /**
     * 发票图片
     */
    @ApiModelProperty(required = false, value = "发票图片")
    private String invoiceImageUrl;
    /**
     * 发票流水号
     */
    @ApiModelProperty(required = false, value = "发票流水号")
    private String invoiceSerialNum;
    /**
     * 发票代码
     */
    @ApiModelProperty(required = false, value = "发票代码")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @ApiModelProperty(required = false, value = "发票号码")
    private String invoiceNum;



}
