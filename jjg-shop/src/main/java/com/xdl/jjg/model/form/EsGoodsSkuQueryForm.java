package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-05-29
 */
@Data
@Api
public class EsGoodsSkuQueryForm extends QueryPageForm {


	/**
     * 商品编号\商品名称
	 */
	@ApiModelProperty(value = "输入框输入值")
	private String keyword;

	/**
     * 商品分类路径
	 */
	@ApiModelProperty(value = "商品分类路径")
	private String categoryPath;

}
