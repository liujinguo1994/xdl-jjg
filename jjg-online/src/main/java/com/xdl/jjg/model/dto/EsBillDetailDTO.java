package com.xdl.jjg.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:47
 */
@Data
@Accessors(chain = true)
public class EsBillDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	private Long id;

    /**
     * 订单编号
     */
	private String orderSn;

	/**
	 * 账单编号
	 */
	private String billSn;

	/**
	 * 关键字查询
	 */
	private String keyword;

    /**
     * 结算状态 0 已结算 1 未结算
     */
	private Integer state;

    /**
     * 类型(1：店铺 2：签约公司 3：供应商 )
     */
	private Integer type;

    /**
     * 签约公司结算单ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long signCompanyId;

    /**
     * 供应商结算ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long supplierBillId;

	/**
	 * 店铺ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long shopId;

	protected Serializable pkVal() {
		return this.id;
	}

}
