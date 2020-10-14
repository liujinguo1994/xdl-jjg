package com.jjg.member.model.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 签约公司结算单
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:40
 */
@Data
@Accessors(chain = true)
public class EsBillDTO implements Serializable {

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

//    /**
//     * 公司ID
//     */
//	@JsonSerialize(using = ToStringSerializer.class)
//	private Long companyId;

//    /**
//     * 公司名称
//     */
//	private String companyName;

    /**
     * 出账日期
     */
	private Long createTime;

    /**
     * 付款时间
     */
	private Long updateTime;

	/**
	 * 出账时间开始
	 */
	private Long startCreateTime;

	/**
	 * 出账时间结束
	 */
	private Long endCreateTime;

	/**
	 * 类型
	 */
	private Integer type;

	protected Serializable pkVal() {
		return this.id;
	}

}
