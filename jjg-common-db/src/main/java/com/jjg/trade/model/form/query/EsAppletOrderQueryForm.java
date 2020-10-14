package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * <p>
 *  移动端-订单查询
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Data
@ApiModel
public class EsAppletOrderQueryForm extends QueryPageForm {
	private static final long serialVersionUID = -5499614164517060537L;

	@ApiModelProperty(value = "订单状态  CONFIRM:待付款,PAID_OFF:待发货,SHIPPED:待收货，COMPLETE:已完成 ，ROG:已收货,CANCELLED:已取消")
	private String orderState;

	@ApiModelProperty(required = true,value = "登录态标识")
	@NotBlank(message = "登录态标识不能为空")
	private String skey;
}
