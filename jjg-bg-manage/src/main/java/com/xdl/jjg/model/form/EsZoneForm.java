package com.xdl.jjg.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;


/**
 * <p>
 * 专区管理
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-05-12
 */
@Data
@ApiModel
public class EsZoneForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 专区名称
     */
    @ApiModelProperty(required = true, value = "专区名称")
    @NotBlank(message = "专区名称不能为空")
    private String zoneName;

    /**
     * 标题
     */
    @ApiModelProperty(required = true, value = "标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String subtitle;

    /**
     * 图片1
     */
    @ApiModelProperty(value = "图片1")
    private String picture1;

    /**
     * 图片2
     */
    @ApiModelProperty(value = "图片2")
    private String picture2;

}
