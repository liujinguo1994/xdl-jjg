package com.xdl.jjg.model.form;


import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * <p>
 * 品质好货
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-08-06
 */
@Data
@ApiModel
public class EsGoodGoodsQueryForm extends QueryPageForm {

private static final long serialVersionUID = 1L;


	/**
	 * 商品名称
	 */
	@ApiModelProperty(value = "goodsName")
	private String goodsName;

	/**
	 * 状态(1:待发布,2.已发布,3:已下架)
	 */
	@ApiModelProperty(value = "状态(1:待发布,2.已发布,3:已下架)",example = "1")
	private Integer state;

	}
