package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * Form
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsGoodsArchQueryForm extends QueryPageForm {
	/**
	 * 输入框输入值（商品编号或者商品名称）
	 */
	@ApiModelProperty(value = "输入框输入值")
	private String keyword;
}
