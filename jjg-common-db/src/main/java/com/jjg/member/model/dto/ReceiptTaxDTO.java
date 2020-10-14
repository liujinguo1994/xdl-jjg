package com.jjg.member.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 电子发票
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class ReceiptTaxDTO implements Serializable {
    /**
     * 电子发票
     */
    EsReceiptTaxDTO order;
}
