package com.xdl.jjg.model.form;

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
 * @since 2019-06-04
 */
@Data
@Api
public class EsMemberCommentCopyForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品id
     */
    @ApiModelProperty(required = false,value = "商品id",example = "1")
    private Long goodsId;

    /**
     * skuid
     */
    @ApiModelProperty(required = false,value = "skuid",example = "1")
    private Long skuId;

    /**
     * 卖家id
     */
    @ApiModelProperty(required = false,value = "卖家id",example = "1")
    private Long shopId;

    /**
     * 评论内容
     */
    @ApiModelProperty(required = false,value = "评论内容",example = "1")
    private String content;

    /**
     * 是否有图片 1 有 2没有
     */
    @ApiModelProperty(required = false,value = "是否有图片 1 有 2没有",example = "1")
    private Integer haveImage;

    /**
     * 订单明细编号
     */
    @ApiModelProperty(required = false,value = "订单明细编号",example = "1")
    private String orderSn;
    /**
     * 评论标签
     */
    @ApiModelProperty(required = false,value = "评论标签",example = "1,2,3")
    private String labels;
    /**
     * 发货速度评分
     */
    @ApiModelProperty(required = false,value = "发货速度评分:1、2、3、4、5",example = "1")
    private Double deliveryScore;

    /**
     * 描述相符度评分
     */
    @ApiModelProperty(required = false,value = "描述相符度评分:1、2、3、4、5",example = "1")
    private Double descriptionScore;

    /**
     * 服务评分
     */
    @ApiModelProperty(required = false,value = "服务评分:1、2、3、4、5",example = "1")
    private Double serviceScore;
    /**
     * 图片地址
     */
    @ApiModelProperty(required = false,value = "图片地址",example = "1,2,3")
    private List<CommentImageForm> commentImageForm;
    /**
     * 是否匿名
     */
    @ApiModelProperty(required = false,value = "1不匿名，2匿名",example = "1")
    private Boolean isAnonymous;
}
