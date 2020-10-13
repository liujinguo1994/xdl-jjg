package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 依据发票流水号查询结果
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-31
 */
@Data
public class QueryByInvoiceSerialNumDO implements Serializable {

    private String orderNo;

    private String taxExcludedAmount;

    private String invoiceDate;

    private String buyerName;

    private String invoiceCode;

    private String invoiceNum;

    private String resultMsg;

    private String invoiceSerialNum;

    private String statusMsg;

    private String buyerTaxNum;
    /**
     * 发票文件路径
     */
    private String invoiceFileUrl;

    private String invoiceLine;

    private String taxIncludedAmount;

    private String status;
    /**
     * 发票地址图片
     */
    private String invoiceImageUrl;
}
