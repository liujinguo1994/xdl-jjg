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
 * @author yuanj 595831329@qq.com
 * @since 2020-03-26
 */
@Data
@ApiModel
public class EsWapMemberCouponForm extends QueryPageForm {


	private static final long serialVersionUID = -5499614164517060537L;

    /**
     * 查询类型
     */
	@ApiModelProperty(value = "查询类型 1:未使用, 2:已使用,3失效")
	private Integer type;
}
