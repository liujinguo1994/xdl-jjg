package com.xdl.jjg.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 发票流水号查询信息
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-31
 */
@Data
public class InvoiceSerialDO implements Serializable {

    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String describe;
    /**
     * 流水号
     */
    private InvoiceSerialNumDO result;


}
