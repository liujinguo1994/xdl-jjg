package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 电子发票传值实体类
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsReceiptTaxDTO implements Serializable {

    /**
     * 电子发票明细，支持填写商品明细最大100行（包含折扣行、被折扣行）
     */
    List<ReceiptTaxInvoiceDetailDTO> invoiceDetail;
    /**
     * 购方电话
     */
    private String buyerTel;
    /**
     * 清单标志:0,根据项目名称数，自动产生清单;1,将项目信息打印至清单
     */
    private String listFlag;
    /**
     * 推送方式:-1,不推送;0,邮箱;1,手机（默认）;2,邮箱、手机
     */
    private String pushMode;
    /**
     * 部门门店id（诺诺系统中的id）
     */
    private String departmentId;
    /**
     * 开票员id（诺诺系统中的id）
     */
    private String clerkId;
    /**
     * 复核人
     */
    private String checker;
    /**
     * 冲红时，在备注中注明“对应正数发票代码:XXXXXXXXX号码:YYYYYYYY”文案，其中“X”为发票代码，“Y”为发票号码，可以不填，接口会自动添加该文案
     */
    private String remark;
    /**
     * 收款人
     */
    private String payee;
    /**
     * 购方地址
     */
    private String buyerAddress;
    /**
     * 购方税号（企业要填，个人可为空）
     */
    private String buyerTaxNum;
    /**
     * 开票类型:1,正票;2,红票
     */
    private String invoiceType;
    /**
     * 发票种类:p,普通发票(电票)(默认);c,普通发票(纸票);s,专用发票;e,收购发票(电票);f,收购发票(纸质)
     */
    private String invoiceLine;
    /**
     * 推送邮箱（pushMode为0或2时，此项为必填）
     */
    private String email;
    /**
     * 销方银行账号和开户行地址
     */
    private String salerAccount;
    /**
     * 订单号（每个企业唯一）
     */
    private String orderNo;
    /**
     * 销方电话
     */
    private String salerTel;
    /**
     * 购方名称
     */
    private String buyerName;
    /**
     * 订单时间
     */
    private String invoiceDate;
    /**
     * 冲红时填写的对应蓝票发票代码（红票必填，不满12位请左补0）
     */
    private String invoiceCode;
    /**
     * 冲红时填写的对应蓝票发票号码（红票必填，不满8位请左补0）
     */
    private String invoiceNum;
    /**
     * 销方地址
     */
    private String salerAddress;
    /**
     * 开票员
     */
    private String clerk;
    /**
     * 购方手机（开票成功会短信提醒购方，不受推送方式影响）
     */
    private String buyerPhone;
    /**
     * 购方银行账号及开户行地址
     */
    private String buyerAccount;
    /**
     * 成品油标志:0,非成品油(默认);1,成品油
     */
    private String productOilFlag;
    /**
     * 销方税号（使用沙箱环境请求时消息体参数salerTaxNum和消息头参数userTax填写339901999999142）
     */
    private String salerTaxNum;
    /**
     * 清单项目名称:打印清单时对应发票票面项目名称（listFlag为1是，此项为必填，默认为“详见销货清单”）
     */
    private String listName;
    /**
     * 代开标志:0非代开;1代开。代开蓝票时备注要求填写文案：代开企业税号:***,代开企业名称:***；代开红票时备注要求填写文案：对应正数发票代码:***号码:***代开企业税号:***代开企业名称:***
     */
    private String proxyInvoiceFlag;
}
