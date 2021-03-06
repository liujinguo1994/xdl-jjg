package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;

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
@EqualsAndHashCode(callSuper = false)
public class EsBillDetailDO extends Model<EsBillDetailDO> {

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
	 * 店铺名称
	 */
	private String shopName;

	/**
	 * 佣金
	 */
	private Double commission;

	/**
	 * 退款金额
	 */
	private Double refundMoney;

	/**
	 * 退还佣金
	 */
	private Double refundCommission;

	/**
	 * 结算时间
	 */
	private Long updateTime;

	/**
	 * 创建日期
	 */
	private Long createTime;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
