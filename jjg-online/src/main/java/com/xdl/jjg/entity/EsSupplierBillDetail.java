package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

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
@TableName("es_supplier_bill_detail")
public class EsSupplierBillDetail extends Model<EsSupplierBillDetail> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键id
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 商品名称
	 */
	@TableField("goods_name")
	private String goodsName;

	/**
	 * 商品Id
	 */
	@TableField("goods_id")
	private Long goodsId;

	/**
	 * 数量
	 */
	@TableField("num")
	private Integer num;

	/**
	 * 供应商id
	 */
	@TableField("supplier_id")
	private Long supplierId;

	/**
	 * 结算单id
	 */
	@TableField("bill_id")
	private Long billId;

	/**
	 * 状态 0 未结算
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 订单编号
	 */
	@TableField("order_sn")
	private String orderSn;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
