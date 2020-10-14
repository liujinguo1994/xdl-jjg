package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
public class EsSettlementDTO implements Serializable {

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
	@JsonSerialize(using = ToStringSerializer.class)
	private Long startTime;

    /**
     * 结算结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
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
	private Long createTime;

    /**
     * 付款时间
     */
	private Long updateTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
