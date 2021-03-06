package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 供应商结算单-es_supplier
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 15:33:45
 */
@Data
@Accessors(chain = true)
@TableName("es_supplier_bill")
public class EsSupplierBill extends Model<EsSupplierBill> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;

	/**
	 * 结算单编号
	 */
	@TableField("bill_sn")
	private String billSn;

	/**
	 * 结算开始时间
	 */
	@TableField("start_time")
	private Long startTime;

	/**
	 * 结算结束时间
	 */
	@TableField("end_time")
	private Long endTime;

	/**
	 * 结算总金额
	 */
	@TableField("price")
	private Double price;

	/**
	 * 结算金额
	 */
	@TableField("bill_money")
	private Double billMoney;

	/**
	 * 状态 0 已结算 1 未结算
	 */
	@TableField("state")
	private Integer state;

	/**
	 * 店铺id
	 */
	@TableField("shop_id")
	private Long shopId;

	/**
	 * 店铺名称
	 */
	@TableField("shop_name")
	private String shopName;

	/**
	 * 结算时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	/**
	 * 创建日期
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * idList
	 */
	@TableField(exist = false)
	private List<String> idList;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
