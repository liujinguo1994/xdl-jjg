package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 商品折扣活动表QueryForm
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-28 14:26:06
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGoodsDiscountQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 活动标题
     */
	@ApiModelProperty(value = "活动标题")
	private String title;

}
