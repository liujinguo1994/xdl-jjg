package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class EsOftenGoodsQueryForm extends QueryPageForm {

    private static final long serialVersionUID = 1L;

    /**
     * 自定义分类id
     */
    @ApiModelProperty(value = "自定义分类id")
    private Long customCategoryId;

}
