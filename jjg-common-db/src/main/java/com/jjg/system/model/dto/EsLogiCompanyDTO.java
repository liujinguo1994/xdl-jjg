package com.jjg.system.model.dto;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-04
 */
@Data
@ToString
public class EsLogiCompanyDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 物流公司名称
     */
    private String name;

    /**
     * 物流公司code
     */
    private String code;

    /**
     * 快递鸟物流公司code
     */
    private String kdcode;

    /**
     * 是否支持电子面单1：支持 0：不支持
     */
    private Integer isWaybill;

    /**
     * 物流公司客户号
     */
    private String customerName;

    /**
     * 物流公司电子面单密码
     */
    private String customerPwd;

    /**
     * 是否有效 0 有效 1 无效
     */
    private Integer state;
    /**
     * 官方电话
     */
    private String phone;
}
