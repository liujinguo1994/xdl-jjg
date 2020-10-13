package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * <p>
 * 自定义分类
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-06
 */
@Data
@ApiModel
public class EsCustomCategoryForm implements Serializable {
    private static final long serialVersionUID = -1775424599865901763L;
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 自定义分类名称
     */

    @ApiModelProperty(required = true, value = "自定义分类名称")
    @NotBlank(message = "自定义分类名称不能为空")
    private String categoryName;

    /**
     * 所属专区ID
     */
    @ApiModelProperty(required = true, value = "所属专区ID")
    @NotNull(message = "所属专区不能为空")
    private Long zoneId;
}
