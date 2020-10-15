package com.jjg.member.model.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 依据发票流水号查询结果
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-31
 */
@Data
public class QueryByInvoiceSerialDO implements Serializable {

    /**
     * 编码
     */
    private String code;
    /**
     * 描述
     */
    private String describe;
    /**
     * 发票地址
     */
    private List<QueryByInvoiceSerialNumDO> result;


}
