package com.xdl.jjg.model.form;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleCategoryQueryForm extends QueryPageForm {


	private static final long serialVersionUID = 1313700039157349982L;

	/**
	 * 分类名称
	 */
	@ApiModelProperty(value = "分类名称")
	private String name;

	}
