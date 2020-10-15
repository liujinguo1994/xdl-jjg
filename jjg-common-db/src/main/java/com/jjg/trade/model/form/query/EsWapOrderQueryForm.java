package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  移动端-订单查询
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-14
 */
@Data
@ApiModel
public class EsWapOrderQueryForm extends QueryPageForm {


	private static final long serialVersionUID = -5499614164517060537L;

    /**
     * 订单状态
     */
	@ApiModelProperty(value = "订单状态  CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货,CANCELLED:已取消")
	private String orderState;
}
