package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;

/**
 * <p>
 * 品牌
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-06-05 17:30:01
 */
@Data
@ApiModel
public class EsBrandForm implements Serializable {

    private static final long serialVersionUID = 2402732215682477885L;
    /**
     * 品牌名称
     */
    @ApiModelProperty(required = true, value = "品牌名称")
    @NotBlank(message = "品牌名称不能为空")
    private String name;

    /**
     * 品牌图标
     */
    @ApiModelProperty(required = true, value = "品牌图标")
    @NotBlank(message = "品牌图标不能为空")
    private String logo;

}
