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
 * 签约公司结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 18:20:13
 */
@Data
@Accessors(chain = true)
@TableName("es_bill")
public class EsBill extends Model<EsBill> {

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
	 * 状态 0 已结算 1 未结算
	 */
	@TableField("state")
	private Integer state;

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

//	/**
//	 * 公司ID
//	 */
//	@TableField("company_id")
//	private Long companyId;

//	/**
//	 * 公司名称
//	 */
//	@TableField("company_name")
//	private String companyName;

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
	 * 结算类型
	 */
	@TableField("type")
	private int type;

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
