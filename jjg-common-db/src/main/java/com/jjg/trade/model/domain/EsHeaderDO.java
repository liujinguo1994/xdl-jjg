package com.jjg.trade.model.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 店铺详细
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-06-24
 */
@Data
public class EsHeaderDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 组织名称
     */
    private String orgName;

    /**
     * 银行开户名
     */
    private String bankAccountName;

    /**
     * 银行开户账号
     */
    private String bankNumber;

    /**
     * 开户银行支行名称
     */
    private String bankName;

    /**
     * 开户银行所在省名称
     */
    private String bankProvince;

    /**
     * 开户银行所在市名称
     */
    private String bankCity;

    /**
     * 开户银行所在县名称
     */
    private String bankCounty;

    /**
     * 开户银行所在镇民名称
     */
    private String bankTown;

    /**
     * 结算总金额
     */
    private Double price;

    /**
     * 结算金额
     */
    private Double billMoney;

    /**
     * 佣金
     */
    private Double commission;

    /**
     * 退款金额
     */
    private Double refundMoney;

    /**
     * 退还佣金
     */
    private Double refundCommission;
}
