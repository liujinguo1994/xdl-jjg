package com.xdl.jjg.model.form;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * <p>
 * 小程序-分页查询会员优惠券
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-07-02
 */
@Data
@ApiModel
public class EsAppletMemberCouponForm extends QueryPageForm {
	private static final long serialVersionUID = -5499614164517060537L;

	@ApiModelProperty(required = true,value = "登录态标识")
	@NotBlank(message = "登录态标识不能为空")
	private String skey;

	@ApiModelProperty(value = "查询类型 1:未使用, 2:已使用,3失效")
	@NotNull(message = "查询类型不能为空")
	private Integer type;
}
