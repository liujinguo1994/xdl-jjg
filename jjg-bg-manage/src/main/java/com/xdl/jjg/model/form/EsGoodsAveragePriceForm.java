package com.xdl.jjg.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("商品客单价")
public class EsGoodsAveragePriceForm extends QueryPageForm {
    private static final long serialVersionUID = 1L;

    /**
     * 分类ID
     */
    @ApiModelProperty(value = "分类ID", example = "")
    private Long categoryId;
    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称", example = "")
    private String categoryName;
    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "所属店铺", example = "")
    private String shopName;
    /**
     * 销售时间开始
     */
    @ApiModelProperty(value = "销售时间区间筛选-开始", example = "")
    private Long saleTimeStart;
    /**
     * 销售时间结束
     */
    @ApiModelProperty(value = "销售时间区间筛选-结束", example = "")
    private Long saleTimeEnd;
}
