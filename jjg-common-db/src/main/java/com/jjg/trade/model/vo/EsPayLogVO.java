package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 支付记录日志表
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@ToString
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsPayLogVO implements Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@ApiModelProperty(value = "主键ID")
	private Long id;
    /**
     * 订单编号
     */
	@ApiModelProperty(value = "订单编号")
	private String orderSn;
    /**
     * 支付类型(支付宝、微信、银联)
     */
	@ApiModelProperty(value = "支付类型(支付宝、微信、银联)")
	private String payType;
    /**
     * 付款时间
     */
	@ApiModelProperty(value = "付款时间")
	private Long payTime;
    /**
     * 付款金额
     */
	@ApiModelProperty(value = "付款金额")
	private Double payMoney;
    /**
     * 付款会员名
     */
	@ApiModelProperty(value = "付款会员名")
	private String payMemberName;
    /**
     * 付款状态
     */
	@ApiModelProperty(value = "付款状态")
	private String payState;
    /**
     * 收款单流水号
     */
	@ApiModelProperty(value = "收款单流水号")
	private String payLogSn;


	protected Serializable pkVal() {
		return this.id;
	}

}
