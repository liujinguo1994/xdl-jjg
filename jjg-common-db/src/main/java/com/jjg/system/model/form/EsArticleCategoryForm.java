package com.jjg.system.model.form;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 文章分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleCategoryForm implements Serializable {


    private static final long serialVersionUID = -9077586804491249109L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID", example = "1")
    private Long id;

    /**
     * 分类名称
     */
    @ApiModelProperty(required = true, value = "分类名称")
    @NotBlank(message = "分类名称不能为空")
    private String name;

    /**
     * 父分类id
     */
    @ApiModelProperty(value = "父分类id", example = "1")
    private Long parentId;

    /**
     * 排序
     */
    @ApiModelProperty(required = true, value = "排序", example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

}
