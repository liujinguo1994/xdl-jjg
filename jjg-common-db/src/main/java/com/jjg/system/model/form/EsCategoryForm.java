package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * Form
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel(description = "商品分类")
public class EsCategoryForm implements Serializable {


    private static final long serialVersionUID = -3317812021081644011L;
    /**
     * 分类名称
     */
    @ApiModelProperty(required = true, value = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 分类父id
     */
    @ApiModelProperty(value = "分类父id", example = "1")
    private Long parentId;

    /**
     * 分类排序
     */
    @ApiModelProperty(value = "分类排序", example = "1")
    private Integer categoryOrder;

    /**
     * 分类图标
     */
    @ApiModelProperty(value = "分类图标")
    private String image;

}
