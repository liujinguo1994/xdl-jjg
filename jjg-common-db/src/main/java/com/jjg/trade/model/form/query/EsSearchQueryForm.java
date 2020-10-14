package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 *  小程序-搜索
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-06-22
 */
@Data
@ApiModel
public class EsSearchQueryForm extends QueryPageForm {
    private static final long serialVersionUID = -4900547578929199527L;

    @ApiModelProperty(value = "关键字")
    private String keyword;

    @ApiModelProperty(name = "sort", value = "排序",allowableValues = "def_asc,def_desc,price_asc,price_desc,buynum_asc,buynum_desc")
    private String sort;

    @ApiModelProperty(value = "品牌List")
    private String brandList;

    @ApiModelProperty(value = "分类IdList")
    private String categoryIdList;
}
