package com.xdl.jjg.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 签约公司结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:40
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsBillDO extends Model<EsBillDO> {

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
     * 状态 0 已结算 1 未结算
     */
	private Integer state;

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
	private Double money;

    /**
     * 结算金额
     */
	private Double billMoney;

    /**
     * 公司ID
     */
	private Long companyId;

    /**
     * 公司名称
     */
	private String companyName;

    /**
     * 出账日期
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long createTime;

    /**
     * 付款时间
     */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Long updateTime;

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
	 * 结算类型
	 */

	private int type;

	/**
	 * idList
	 */

	private List<String> idList;

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
