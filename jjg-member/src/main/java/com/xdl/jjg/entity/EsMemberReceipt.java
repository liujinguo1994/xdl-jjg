package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

/**
 * <p>
 * 会员发票信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_member_receipt")
public class EsMemberReceipt extends Model<EsMemberReceipt> {

    private static final long serialVersionUID = 1L;

    /**
     * 会员发票id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
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
     * 发票内容
     */
    @TableField("receipt_content")
	private String receiptContent;
    /**
     * 发票税号
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
     * 是否为默认
     */
    @TableField("is_default")
	private Integer isDefault;
    /**
     * 社会信用统一代码
     */
    @TableField("credit_code")
    private String creditCode;
    /**
     * 单位名称
     */
    private String department;
    /**
     * 企业名称
     */
    private String company;
    /**
     * 商品明细
     */
    @TableField("receipt_details")
    private Integer receiptDetails;

}
