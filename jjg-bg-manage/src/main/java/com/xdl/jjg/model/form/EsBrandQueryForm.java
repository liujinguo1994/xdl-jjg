package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 品牌
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsBrandQueryForm extends QueryPageForm {

	private static final long serialVersionUID = 2402732215682477885L;
	/**
     * 品牌名称
     */
	@ApiModelProperty(value = "品牌名称")
	private String name;

}
