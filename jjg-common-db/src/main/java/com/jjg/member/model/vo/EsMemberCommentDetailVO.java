package com.jjg.member.model.vo;


import com.jjg.member.model.domain.EsCommentGalleryDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 评论
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
public class EsMemberCommentDetailVO implements Serializable {


    /**
     * 评论主键
     */
    @ApiModelProperty(value = "主键id",example = "1")
    private Long id;
    /**
     * 会员头像
     */
    @ApiModelProperty(required = false,value = "会员头像")
    private String face;
    /**
     * 会员手机号
     */
    @ApiModelProperty(required = false,value = "会员手机号")
    private String mobile;
    /**
     * 好中差评
     */
    @ApiModelProperty(required = false,value = "好中差评")
    private String grade;
    /**
     * 综合评分
     */
    @ApiModelProperty(required = false,value = "综合评分")
    private Double commentScore;
    /**
     * 评论时间
     */
    @ApiModelProperty(required = false,value = "评论时间")
    private Long createTime;
    /**
     * 商品id
     */
    @ApiModelProperty(required = false,value = "商品id")
    private Long goodsId;
    /**
     * 评论图片路径
     */
    @ApiModelProperty(required = false,value = "评论图片路径")
    private List<EsCommentGalleryDO> commentsImage;
    /**
     * 商品标签
     */
    @ApiModelProperty(required = false,value = "商品标签")
    private String goodTag;
    /**
     * 点赞数量
     */
    @ApiModelProperty(required = false,value = "点赞数量")
    private Integer supportNum;
    /**
     * 商品编号
     */
    @ApiModelProperty(required = false,value = "商品编号")
    private String goodsSn;
    /**
     * 买家昵称
     */
    @ApiModelProperty(required = false,value = "买家昵称")
    private String nickname;
    /**
     * 评论标签
     */
    @ApiModelProperty(required = false,value = "评论标签")
    private String labels;
    /**
     * 评论标签内容
     */
    @ApiModelProperty(required = false,value = "评论标签内容")
    private List<String> labelsName;
    /**
     * 商家回复
     */
    @ApiModelProperty(required = false,value = "商家回复")
    private EsCommentReplyVO replayContent;
    /**
     * 追加评论
     */
    @ApiModelProperty(required = false,value = "追加评论")
    private EsAddCommentVO addContent;

    /**
     * 评论内容
     */
    @ApiModelProperty(required = false,value = "评论内容")
    private String content;

    /**
     * 商家回复内容
     */
    @ApiModelProperty(required = false,value = "商家回复内容")
    private String replayContentInfo;

    /**
     * 追加评论内容
     */
    @ApiModelProperty(required = false,value = "追加评论内容")
    private String addContentInfo;

    /**
     * 是否点赞 0：否 1：是
     */
    @ApiModelProperty(value = "是否已点赞 0：否 1：是")
    private Integer judgeCommentSupport;
    @ApiModelProperty(value = "匿名(2:匿名，1:否)")
    private Long isAnonymous;
}
