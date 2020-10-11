package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 会员投诉信息
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsComplaintForm extends EsQueryPageForm {

	private static final long serialVersionUID = 5697156789959653573L;
	/**
	 * 关键字(订单编号，商家名称)
	 */
	@ApiModelProperty(value = "关键字(订单编号，商家名称)")
	private String keyword;
	/**
	 * 投诉类型
	 */
	@ApiModelProperty(value = "投诉类型")
	private Long typeId;

}
