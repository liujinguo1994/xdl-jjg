package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * <p>
 * 文章
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2019-07-24
 */
@Data
@ApiModel
public class EsArticleVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    private Long id;

    /**
     * 文章名称
     */
    @ApiModelProperty(value = "文章名称")
    private String articleName;

    /**
     * 分类id
     */
    @ApiModelProperty(value = "分类id")
    private Long categoryId;

    /**
     * 文章排序
     */
    @ApiModelProperty(value = "文章排序")
    private Integer sort;

    /**
     * 外链url
     */
    @ApiModelProperty(value = "外链url")
    private String outsideUrl;

    /**
     * 文章内容
     */
    @ApiModelProperty(value = "文章内容")
    private String content;

    /**
     * 显示位置
     */
    @ApiModelProperty(value = "显示位置")
    private String showPosition;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "修改时间")
    private Long updateTime;

    /**
     * 分类名称
     */
    @ApiModelProperty(value = "分类名称")
    private String name;

}
