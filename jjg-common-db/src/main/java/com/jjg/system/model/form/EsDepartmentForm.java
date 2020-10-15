package com.jjg.system.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 部门
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "部门")
public class EsDepartmentForm implements Serializable {

    private static final long serialVersionUID = 4589539848847575716L;

    /**
     * 部门名称
     */
    @ApiModelProperty(required = true, value = "部门名称")
    @NotBlank(message = "部门名称不能为空")
    private String departmentName;

    /**
     * 父id
     */
    @ApiModelProperty(required = true, value = "父id", example = "1")
    @NotNull(message = "父id不能为空")
    private Long parentId;

}
