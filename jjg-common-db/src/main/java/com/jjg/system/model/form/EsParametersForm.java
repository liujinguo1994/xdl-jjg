package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 参数
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsParametersForm implements Serializable {


    private static final long serialVersionUID = 6810271004569935295L;
    /**
     * 参数名称
     */
    @ApiModelProperty(required = true, value = "参数名称")
    @NotBlank(message = "参数名称不能为空")
    private String paramName;

    /**
     * 参数类型1 输入项   2 选择项
     */
    @ApiModelProperty(required = true, value = "参数类型1 输入项   2 选择项", example = "1")
    @NotNull(message = "参数类型不能为空")
    private Integer paramType;

    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
    @ApiModelProperty(value = "选择值，当参数类型是选择项2时，必填，逗号分隔")
    private String options;

    /**
     * 是否可索引，0 不显示 1 显示
     */
    @ApiModelProperty(required = true, value = "是否可索引，0 不显示 1 显示", example = "1")
    @NotNull(message = "是否可索引不能为空")
    private Integer isIndex;

    /**
     * 是否必填是  1    否   0
     */
    @ApiModelProperty(required = true, value = "是否必填是  1    否   0", example = "1")
    @NotNull(message = "是否必填不能为空")
    private Integer required;

    /**
     * 参数分组id
     */
    @ApiModelProperty(required = true, value = "参数分组id", example = "1")
    @NotNull(message = "参数分组id不能为空")
    private Long groupId;

    /**
     * 商品分类id
     */
    @ApiModelProperty(required = true, value = "商品分类id", example = "1")
    @NotNull(message = "商品分类id不能为空")
    private Long categoryId;

}
