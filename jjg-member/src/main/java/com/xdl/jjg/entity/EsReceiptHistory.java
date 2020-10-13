package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 发票历史
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_receipt_history")
public class EsReceiptHistory extends Model<EsReceiptHistory> {

    private static final long serialVersionUID = 1L;

    /**
     * 发票id
     */
	@TableId(value="history_id", type= IdType.AUTO)
	private Long historyId;
    /**
     * 订单编号
     */
    @TableField("order_sn")
	private String orderSn;
    /**
     * 卖家id
     */
    @TableField("shop_id")
	private Long shopId;
    /**
     * 会员id
     */
    @TableField("member_id")
	private Long memberId;
    /**
     * 发票类型
     */
    @TableField("receipt_type")
	private String receiptType;
    /**
     * 发票抬头
     */
    @TableField("receipt_title")
	private String receiptTitle;
    /**
     * 发票金额
     */
    @TableField("receipt_amount")
	private Double receiptAmount;
    /**
     * 发票内容
     */
    @TableField("receipt_content")
	private String receiptContent;
    /**
     * 税号
     */
    @TableField("tax_no")
	private String taxNo;
    /**
     * 注册地址
     */
    @TableField("reg_addr")
	private String regAddr;
    /**
     * 注册电话
     */
    @TableField("reg_tel")
	private String regTel;
    /**
     * 开户银行
     */
    @TableField("bank_name")
	private String bankName;
    /**
     * 银行账户
     */
    @TableField("bank_account")
	private String bankAccount;
    /**
     * 开票时间
     */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	private Long createTime;
    /**
     * 会员名称
     */
    @TableField("member_name")
	private String memberName;
    /**
     * 开票状态
     */
	private Integer state;
    /**
     * 失败原因
     */
    @TableField("fail_reason")
	private String failReason;
    /**
     * 商品id
     */
    @TableField("goods_id")
    private Long goodsId;
    /**
     * 发票图片路径
     */
    @TableField("invoice_file_url")
    private String invoiceFileUrl;
    /**
     * 发票图片
     */
    @TableField("invoice_image_url")
    private String invoiceImageUrl;
    /**
     * 发票流水号
     */
    @TableField("invoice_serial_num")
    private String invoiceSerialNum;
    /**
     * 发票代码
     */
    @TableField("invoice_code")
    private String invoiceCode;
    /**
     * 发票号码
     */
    @TableField("invoice_num")
    private String invoiceNum;



}
