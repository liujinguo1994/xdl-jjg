package com.xdl.jjg.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 投诉条件
 * </p>
 *
 * @author yuanj 595831328@qq.com
 * @since 2019-08-13
 */
@Data
@ToString
public class ComplaintQueryParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 投诉类型
     */
    private Long typeId;

    /**
     * 订单号
     */
    private String orderSn;

    /**
     * 店铺名称
     */
    private String shopName;




}
