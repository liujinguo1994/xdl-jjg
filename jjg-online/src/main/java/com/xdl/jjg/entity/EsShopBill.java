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
 * 店铺结算单-es_shop_bill
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-23 15:54:00
 */
@Data
@Accessors(chain = true)
@TableName("es_shop_bill")
public class EsShopBill extends Model<EsShopBill> {

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
	 * 佣金
	 */
	@TableField("commission")
	private Double commission;

	/**
	 * 退款金额
	 */
	@TableField("refund_money")
	private Double refundMoney;

	/**
	 * 退还佣金
	 */
	@TableField("refund_commission")
	private Double refundCommission;

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
