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
 * @author WNAGAF 826988665@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsDraftGoodsQueryForm extends QueryPageForm {


	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "商品名称")
	private String goodsName;
	/**
	 * 分类id
	 */
	@ApiModelProperty(value = "分类路径")
	private String categoryPath;
	private Integer isVirtual;
}
