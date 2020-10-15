package com.jjg.trade.model.form.query;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 *  移动端-订单查询
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2020-03-12
 */
@Data
@ApiModel
public class EsWapMemberOrderForm implements Serializable {


	private static final long serialVersionUID = -5499614164517060537L;

    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态  CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货,CANCELLED:已取消")
	private String orderState;


	/**
	 * 订单状态
	 */
	@ApiModelProperty(value = "订单编号")
	@NotBlank(message = "订单编号不能为空")
	private String orderSn;
}
