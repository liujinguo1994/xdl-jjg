package com.xdl.jjg.model.form;

import com.shopx.trade.api.model.domain.vo.ClientConfigVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 支付方式
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsPaymentMethodForm implements Serializable {

	private static final long serialVersionUID = 8136526494168440239L;

	@ApiModelProperty(value = "支付方式名称")
	private String methodName;

	@ApiModelProperty(value = "支付插件id")
	private String pluginId;

	@ApiModelProperty(value = "支付方式图片")
	private String image;

	@ApiModelProperty(value = "是否支持原路退回，0不支持  1支持")
	@NotNull(message = "请选择是否支持原路退回")
	@Min(value = 0, message = "是否支持原路退回值不正确")
	@Max(value = 1, message = "是否支持原路退回值不正确")
	private Integer isRetrace;

	@ApiModelProperty(value = "配置项")
	@Valid
	@NotNull(message = "客户端开启情况不能为空")
	private List<ClientConfigVO> enableClient;
}
