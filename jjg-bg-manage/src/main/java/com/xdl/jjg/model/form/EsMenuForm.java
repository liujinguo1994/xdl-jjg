package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 *  菜单
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "菜单")
public class EsMenuForm implements Serializable {

    private static final long serialVersionUID = -74985518123532679L;

    /**
     * 父id
     */
    @ApiModelProperty(value = "父id",example = "1")
	private Long parentId;
    /**
     * 菜单标题
     */
    @ApiModelProperty(required = true, value = "菜单标题")
    @NotBlank(message = "菜单标题不能为空")
	private String title;
    /**
     * 菜单唯一标识
     */
    @ApiModelProperty(required = true, value = "菜单唯一标识")
    @NotBlank(message = "菜单唯一标识不能为空")
	private String identifier;
    /**
     * 权限表达式
     */
    @ApiModelProperty(required = true, value = "权限表达式")
    @NotBlank(message = "权限表达式不能为空")
	private String authExpression;

}
