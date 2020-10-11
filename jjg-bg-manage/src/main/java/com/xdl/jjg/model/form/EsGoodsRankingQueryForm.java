package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 热门榜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-07
 */
@Data
@ApiModel
public class EsGoodsRankingQueryForm extends QueryPageForm {

private static final long serialVersionUID = 1L;

	/**
	 * 榜单名称
	 */
	@ApiModelProperty(value = "榜单名称")
	private String rankingName;
}
