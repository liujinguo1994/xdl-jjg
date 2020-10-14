package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员余额明细VO
 * </p>
 *
 * @author LINS 1220316142@qq.com
 * @since 2019-07-23 16:07:58
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberDepositVO implements Serializable {

    private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	@ApiModelProperty(value = "主键ID")
	private Long id;

    /**
     * 操作类型(充值，消费，退款)
     */
	@ApiModelProperty(value = "操作类型(1充值，2消费，3退款)")
	private String type;

    /**
     * 金额
     */
	@ApiModelProperty(value = "金额")
	private Double money;

    /**
     * 操作时间
     */
	@ApiModelProperty(value = "操作时间")
	private Long createTime;

	/**
	 * 会员余额
	 */
	@ApiModelProperty(value = "会员余额")
	private Double memberBalance;

	/**
	 * 订单编号
	 */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;
	/**
	 * 交易编号
	 */
	@ApiModelProperty(value = "交易编号")
	private String tradeSn;

}
