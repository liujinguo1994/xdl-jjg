package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论信息 mvn deploy
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@ApiModel
public class EsCommentInfoVO implements Serializable {


    /**
     * 评论主键
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private Long id;
    /**
     * 评论内容
     */
    @ApiModelProperty(required = false, value = "主键ID")
    private String content;
    /**
     * 好中差评
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1")
    private String grade;
    /**
     * 综合评分
     */
    @ApiModelProperty(required = false, value = "主键ID", example = "1.2")
    private Double commentScore;
    /**
     * 标签信息
     */
    @ApiModelProperty(required = false, value = "标签信息")
    private List<String> tags;
    /**
     * 图片信息
     */
    @ApiModelProperty(required = false, value = "图片信息")
    private List<String> originals;
    /**
     * 评价标签
     */
    @ApiModelProperty(required = false, value = "标签")
    private String labels;



}
