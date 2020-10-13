package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 参数组
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsParameterGroupForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 参数组名称
     */
    @ApiModelProperty(required = true, value = "参数组名称")
    @NotBlank(message = "参数组名称不能为空")
    private String groupName;

    /**
     * 关联分类id
     */
    @ApiModelProperty(required = true, value = "关联分类id", example = "1")
    @NotNull(message = "关联分类id不能为空")
    private Long categoryId;


}
