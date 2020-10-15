package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsSupplierBillDetailDO extends Model<EsSupplierBillDetailDO> {

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
	private Long goodsId;

    /**
     * 数量
     */
	private Integer num;

    /**
     * 供应商id
     */
	private Long supplierId;

    /**
     * 结算单id
     */
	private Long billId;

    /**
     * 状态 0 未结算
     */
	private Integer state;

    /**
     * 订单编号
     */
	private String orderSn;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
