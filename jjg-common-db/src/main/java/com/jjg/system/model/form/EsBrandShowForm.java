package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 品牌展示
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-12 13:23:47
 */
@Data
@ApiModel
public class EsBrandShowForm implements Serializable {

    @ApiModelProperty(required = true, value = "品牌展示名称")
    @NotBlank(message = "品牌展示名称不能为空")
    private String name;

    @ApiModelProperty(required = true, value = "品牌logo链接")
    @NotBlank(message = "品牌logo链接不能为空")
    private String imgUrl;

    @ApiModelProperty(required = true, value = "品牌点击链接")
    @NotBlank(message = "品牌点击链接不能为空")
    private String linkUrl;

    @ApiModelProperty(required = true, value = "排序", example = "1")
    @NotNull(message = "排序不能为空")
    private Integer sort;

}
