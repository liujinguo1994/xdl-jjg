package com.xdl.jjg.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 好中差评论统计
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class GradeLevelVO implements Serializable {

    /**
     * 评论总是
     */
    @ApiModelProperty(required = false,value = "评论总数",example = "1")
    private Integer num ;
    /**
     * 好评
     */
    @ApiModelProperty(required = false,value = "好评总数",example = "1")
    private Integer goodNum;
    /**
     * 中评
     */
    @ApiModelProperty(required = false,value = "中评总数",example = "1")
    private Integer commentNum;
    /**
     * 差评
     */
    @ApiModelProperty(required = false,value = "差评总数",example = "1")
    private Integer badNum;
    /**
     * 带评论图片
     */
    @ApiModelProperty(required = false,value = "带评论图片",example = "1")
    private Integer pictureNum;


}
