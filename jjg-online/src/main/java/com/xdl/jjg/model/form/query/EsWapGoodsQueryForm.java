package com.xdl.jjg.model.form.query;

import com.shopx.common.model.result.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 *
 * @Description: 移动端-商品查询Form
 *
 */
@Data
@ApiModel
public class EsWapGoodsQueryForm extends QueryPageForm {

    /**
     *分类ID
     */
    private Long categoryId;

    /**
     * 店铺id
     */
    private Integer shopId;

    /**
     * 输入值
     */
    @ApiModelProperty(value = "关键字")
    private String keyword;

    /**
     * 价格区间 例如50_100
     */
    private String price;

    /**
     * 排序
     */
    @ApiModelProperty(
            name = "sort",
            value = "排序:关键字_排序",
            allowableValues = "def_asc,def_desc,price_asc,price_desc,buynum_asc,buynum_desc,grade_asc,grade_desc"
    )
    private String sort = "def_asc";
    @ApiModelProperty(value = "品牌ID")
    private List<String> brandList;
    @ApiModelProperty(value = "分类Id")
    private List<String> categoryIdList;
}
