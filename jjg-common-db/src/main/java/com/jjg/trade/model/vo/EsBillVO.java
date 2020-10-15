package com.jjg.trade.model.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 签约公司结算单VO
 * </p>
 *
 * @author LBW 981087977@qq.com
 * @since 2019-06-05 14:28:41
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsBillVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 结算单编号
     */
	@ApiModelProperty(value = "结算单编号")
	private String billSn;

    /**
     * 状态 0 已结算 1 未结算
     */
	@ApiModelProperty(value = "状态 0 已结算 1 未结算")
	private Integer state;

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
     * 结算总金额
     */
	@ApiModelProperty(value = "总金额")
	private Double money;

    /**
     * 结算金额
     */
	@ApiModelProperty(value = "结算金额")
	private Double billMoney;

    /**
     * 公司ID
     */
	@JsonSerialize(using = ToStringSerializer.class)
	@ApiModelProperty(value = "公司ID")
	private Long companyId;

    /**
     * 公司名称
     */
	@ApiModelProperty(value = "公司名称")
	private String companyName;

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


	/**
	 * 佣金
	 */
	@ApiModelProperty(value = "佣金")
	private Double commission;

	/**
	 * 退款金额
	 */
	@ApiModelProperty(value = "退款金额")
	private Double refundMoney;

	/**
	 * 退还佣金
	 */
	@ApiModelProperty(value = "退还佣金")
	private Double refundCommission;

	/**
	 * 结算类型
	 */
	@ApiModelProperty(value = "结算类型")
	private int type;

	/**
	 * idList
	 */
	@ApiModelProperty(value = "idList")
	private List<String> idList;

	protected Serializable pkVal() {
		return this.id;
	}

}
