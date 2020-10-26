package com.jjg.trade.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-08-20
 */
@Data
@Accessors(chain = true)
public class EsSupplierBillDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Integer id;

    /**
     * 商品名称
     */
	private String goodsName;

    /**
     * 商品Id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long goodsId;

    /**
     * 数量
     */
	private Integer num;

    /**
     * 供应商id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long supplierId;

    /**
     * 结算单id
     */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long billId;

    /**
     * 状态 0 未结算
     */
	private Integer state;

    /**
     * 订单编号
     */
	private String orderSn;

	protected Serializable pkVal() {
		return this.id;
	}

}
