package com.jjg.trade.model.vo;

import lombok.Data;
import lombok.experimental.Accessors;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import java.io.Serializable;

/**
 * <p>
 * 结算单VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-08-20 15:12:54
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsSettlementVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
	@ApiModelProperty(value = "主键")
	private Long id;

    /**
     * 结算单编号
     */
	@ApiModelProperty(value = "结算单编号")
	private String settlementSn;

    /**
     * 结算开始时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "结算开始时间")
	private Long startTime;

    /**
     * 结算结束时间
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "结算结束时间")
	private Long endTime;

    /**
     * 总金额
     */
	@ApiModelProperty(value = "总金额")
	private Double money;

    /**
     * 结算金额
     */
	@ApiModelProperty(value = "结算金额")
	private Double billMoney;

    /**
     * 出账日期
     */
	@ApiModelProperty(value = "出账日期")
	private Long createTime;

    /**
     * 付款时间
     */
	@ApiModelProperty(value = "付款时间")
	private Long updateTime;

	protected Serializable pkVal() {
		return this.id;
	}

}
