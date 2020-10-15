package com.jjg.trade.model.form.query;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 常买商品
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ApiModel
public class EsAppOftenAndFindQueryForm extends QueryPageForm {

private static final long serialVersionUID = 1L;

	/**
	 * 自定义分类id
	 */
	@ApiModelProperty(required = true,value = "自定义分类id")
	@NotNull(message = "自定义分类id不能为空")
	private Long customCategoryId;

	}
