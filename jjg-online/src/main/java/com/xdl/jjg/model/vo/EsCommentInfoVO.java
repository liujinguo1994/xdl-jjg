package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
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
@Api
@JsonIgnoreProperties(ignoreUnknown=true)
public class EsCommentInfoVO implements Serializable {


    /**
     * 评论主键
     */
    @ApiModelProperty(required = false, value = "评论主键", example = "1")
    private Long id;
    /**
     * 评论内容
     */
    @ApiModelProperty(required = false, value = "评论内容")
    private String content;
    /**
     * 好中差评
     */
    @ApiModelProperty(required = false, value = "0:好 1:中 2:差", example = "1")
    private String grade;
    /**
     * 综合评分
     */
    @ApiModelProperty(required = false, value = "综合评分", example = "1.2")
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

    // 评价多张图片信息
    @ApiModelProperty(required = false, value = "图片信息")
    private List<CommentImageVO> commentImageVO;

    /**
     * 追评内容
     */
    private EsAddCommentVO addContentVO;
    /**
     * 描述相符度评分
     */
    @ApiModelProperty(required = false, value = "描述相符度评分")
    private Double descriptionScore;
    /**
     * 服务评分
     */
    @ApiModelProperty(required = false, value = "服务评分")
    private Double serviceScore;
    /**
     * 发货速度评分
     */
    @ApiModelProperty(required = false, value = "发货速度评分")
    private Double deliveryScore;
}
