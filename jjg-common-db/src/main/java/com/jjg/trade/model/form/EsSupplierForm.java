package com.jjg.trade.model.form;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * <p>
 * 供应商结算单-es_supplier
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
public class EsSupplierForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;
    /**
     * 结算单编号
     */
	private String billSn;
    /**
     * 结算开始时间
     */
	private Long startTime;
    /**
     * 结算结束时间
     */
	private Long endTime;
    /**
     * 结算总金额
     */
	private BigDecimal price;
    /**
     * 结算金额
     */
	private BigDecimal billMoney;
    /**
     * 状态 0 已结算 1 未结算
     */
	private Integer state;
    /**
     * 店铺id
     */
	private Long shopId;
    /**
     * 店铺名称
     */
	private String shopName;
    /**
     * 结算时间
     */
	private Long payTime;
    /**
     * 创建日期
     */
	private Long createTime;


	protected Serializable pkVal() {
		return this.id;
	}

}
