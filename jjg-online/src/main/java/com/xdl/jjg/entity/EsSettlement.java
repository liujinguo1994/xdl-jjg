package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-20 15:12:52
 */
@Data
@Accessors(chain = true)
@TableName("es_settlement")
public class EsSettlement extends Model<EsSettlement> {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
		private Double id;

	/**
	 * 结算单编号
	 */
	@TableField("settlement_sn")
	private String settlementSn;

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
	 * 总金额
	 */
	@TableField("money")
	private Double money;

	/**
	 * 结算金额
	 */
	@TableField("bill_money")
	private Double billMoney;

	/**
	 * 出账日期
	 */
	@TableField(value = "create_time", fill = FieldFill.INSERT)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

	/**
	 * 付款时间
	 */
	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
