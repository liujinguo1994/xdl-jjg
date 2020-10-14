package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
public class EsZoneVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 专区名称
     */
    @ApiModelProperty(value = "专区名称")
    private String zoneName;

    /**
     * 标题
     */
    @ApiModelProperty(value = "标题")
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
