package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ApiModel
public class EsCustomCategoryQueryForm extends QueryPageForm {
	private static final long serialVersionUID = 2673743529261293617L;

	/**
	 * 所属专区ID
	 */
	@ApiModelProperty(value = "所属专区ID")
	private Long zoneId;
}
