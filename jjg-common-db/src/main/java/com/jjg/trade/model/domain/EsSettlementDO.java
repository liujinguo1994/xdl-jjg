package com.jjg.trade.model.domain;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-20 15:12:54
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EsSettlementDO extends Model<EsSettlementDO> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	private Long id;

    /**
     * 结算单编号
     */
	private String settlementSn;

    /**
     * 结算开始时间
     */
	private Long startTime;

    /**
     * 结算结束时间
     */
	private Long endTime;

    /**
     * 总金额
     */
	private Double money;

    /**
     * 结算金额
     */
	private Double billMoney;

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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}
}
