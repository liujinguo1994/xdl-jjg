package com.xdl.jjg.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 投诉举报图片表
 * </p>
 *
 * @author yuanj 595831329@qq.com
 * @since 2019-07-31
 */
@Data
public class EsComrImglDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
	private Long id;
    /**
     * 类型，0投诉，1举报
     */
    @ApiModelProperty(required = false, value = "类型，0投诉，1举报", example = "1")
	private Integer type;

    /**
     * 缩略图路径
     */
    @ApiModelProperty(required = false, value = "缩略图路径", example = "1")
    private String thumbnail;
    /**
     * 小图路径
     */
    @ApiModelProperty(required = false, value = "小图路径", example = "1")
    private String small;
    /**
     * 大图路径
     */
    @ApiModelProperty(required = false, value = "大图路径", example = "1")
    private String big;
    /**
     * 原图路径
     */
    @ApiModelProperty(required = false, value = "原图路径", example = "1")
    private String original;
    /**
     * 极小图路径
     */
    @ApiModelProperty(required = false, value = "极小图路径", example = "1")
    private String tiny;
    /**
     * 排序
     */
    @ApiModelProperty(required = false, value = "排序", example = "1")
    private Integer sort;
    /**
     * 投诉、举报id
     */
    @ApiModelProperty(required = false, value = "投诉、举报id", example = "1")
    private Long comId;




}
