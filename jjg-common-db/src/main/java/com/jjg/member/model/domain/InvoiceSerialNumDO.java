package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发票流水号
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-31
 */
@Data
public class InvoiceSerialNumDO implements Serializable {

    /**
     * 发票流水号
     */
    private String invoiceSerialNum;
}
