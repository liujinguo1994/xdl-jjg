package com.xdl.jjg.model.form.query;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 支付记录日志表QueryForm
 * </p>
 *
 * @author LiuJG 344009799@qq.com
 * @since 2019-06-05 09:25:43
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsPayLogQueryForm implements Serializable {

    private static final long serialVersionUID = 1L;

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
	@JsonSerialize(using = ToStringSerializer.class)
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

}
