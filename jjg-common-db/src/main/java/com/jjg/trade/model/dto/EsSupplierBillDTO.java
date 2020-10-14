package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 供应商结算单-es_supplier
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:47
 */
@Data
@Accessors(chain = true)
public class EsSupplierBillDTO implements Serializable {

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long startTime;

    /**
     * 结算结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long endTime;

    /**
     * 结算总金额
     */
	private Double price;

    /**
     * 结算金额
     */
	private Double billMoney;

    /**
     * 状态 0 已结算 1 未结算
     */
	private Integer state;

    /**
     * 店铺id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

    /**
     * 店铺名称
     */
	private String shopName;

    /**
     * 结算时间
     */
	private Long updateTime;

    /**
     * 创建日期
     */
	private Long createTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
