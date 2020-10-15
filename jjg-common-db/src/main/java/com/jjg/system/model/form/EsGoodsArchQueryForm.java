package com.jjg.system.model.form;

import com.xdl.jjg.response.web.QueryPageForm;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 商品档案查询form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsGoodsArchQueryForm extends QueryPageForm {


    private static final long serialVersionUID = -5377249503740353326L;

    /**
     * 输入框输入值(供应商名称，负责人姓名，采购经理姓名)
     */
    @ApiModelProperty(value = "输入框输入值(供应商名称，负责人姓名，采购经理姓名)")
    private String keyword;

    /**
     * 创建时间开始
     */
    @ApiModelProperty(value = "创建时间开始", example = "1")
    private Long createTimeStart;

    /**
     * 创建时间结束
     */
    @ApiModelProperty(value = "创建时间结束", example = "1")
    private Long createTimeEnd;

    /**
     * 是否赠品
     */
    @ApiModelProperty(value = "是否赠品 1是 2不是")
    private Integer isGifts;

}
