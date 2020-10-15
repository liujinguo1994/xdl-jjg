package com.jjg.shop.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * <p>
 * 第二件半价活动表QueryForm
 * </p>
 *
 * @author WAF 826988665@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsHalfPriceQueryForm extends QueryPageForm {

    /**
     * 活动标题
     */
    @ApiModelProperty(value = "活动标题")
    private String title;

}
