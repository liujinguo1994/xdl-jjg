package com.jjg.member.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 店铺模版
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel(description = "店铺模版")
public class EsShopThemesForm implements Serializable {


    private static final long serialVersionUID = -6549163021718690704L;
    /**
     * 模版类型(PC ,WAP)
     */
    @ApiModelProperty(required = true, value = "模版类型(PC ,WAP)")
    @NotBlank(message = "模版类型不能为空")
    private String type;

    /**
     * 模版名称
     */
    @ApiModelProperty(required = true, value = "模版名称")
    @NotBlank(message = "模版名称不能为空")
    private String name;

    /**
     * 图片模板路径
     */
    @ApiModelProperty(required = true, value = "图片模板路径")
    @NotBlank(message = "图片模板路径不能为空")
    private String imagePath;

    /**
     * 是否为默认(1是，0否)
     */
    @ApiModelProperty(required = true, value = "是否为默认(1是，0否)", example = "1")
    @NotNull(message = "是否为默认不能为空")
    private Integer isDefault;

    /**
     * 模版标识
     */
    @ApiModelProperty(required = true, value = "模版标识")
    @NotBlank(message = "模版标识不能为空")
    private String mark;
}
