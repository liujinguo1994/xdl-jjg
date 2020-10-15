package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 公司折扣
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsDiscountForm implements Serializable {


    private static final long serialVersionUID = 6942417795393432160L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;
    /**
     * 公司ID
     */
    @ApiModelProperty(required = true, value = "公司ID", example = "1")
    @NotNull(message = "公司ID不能为空")
    private Long companyId;
    /**
     * 商品分类ID
     */
    @ApiModelProperty(required = true, value = "商品分类ID", example = "1")
    @NotNull(message = "商品分类ID不能为空")
    private Long categoryId;
    /**
     * 分类名称
     */
    @ApiModelProperty(required = true, value = "分类名称")
    @NotNull(message = "分类名称不能为空")
    private String categoryName;
    /**
     * 折扣（系数计算）
     */
    @ApiModelProperty(required = true, value = "折扣")
    @NotNull(message = "折扣不能为空")
    private Double discount;

}
