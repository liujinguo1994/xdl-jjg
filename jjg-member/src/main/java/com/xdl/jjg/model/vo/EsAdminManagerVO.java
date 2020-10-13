package com.xdl.jjg.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.shopx.member.api.model.domain.EsMemberSpecValuesDO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 后台管理评论列表
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsAdminManagerVO implements Serializable {


    /**
     * 评论主键
     */
    @ApiModelProperty(required = false,value = "评论主键",example = "1")
    private Long id;
    /**
     * 会员头像
     */
    @ApiModelProperty(required = false,value = "会员头像",example = "1")
    private String face;
    /**
     * 会员手机号
     */
    @ApiModelProperty(required = false,value = "会员手机号",example = "1")
    private String mobile;
    /**
     * 好中差评
     */
    @ApiModelProperty(required = false,value = "好中差评",example = "1")
    private String grade;
    /**
     * 综合评分
     */
    @ApiModelProperty(required = false,value = "综合评分",example = "1.11")
    private Double commentScore;
    /**
     * 评论时间
     */
    @ApiModelProperty(required = false,value = "评论时间",example = "1233")
    private Long createTime;
    /**
     * 商品id
     */
    @ApiModelProperty(required = false,value = "商品id",example = "1")
    private Long goodsId;
    /**
     * 评论图片路径
     */
    @ApiModelProperty(required = false,value = "评论图片",example = "1")
    private List<String> commentsImage;
    /**
     * 商品标签
     */
    @ApiModelProperty(required = false,value = "商品标签")
    private String goodTag;
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
     * 商品规格信息
     */
    @ApiModelProperty(required = false,value = "商品规格信息")
    private String specValues;
    /**
     * 评论内容
     */
    @ApiModelProperty(required = false,value = "评论内容")
    private String content;
    /**
     * 评论回复
     */
    @ApiModelProperty(required = false,value = "评论回复")
    private String replayContent;
    /**
     * 是否回复 1回复，2未回复
     */
    @ApiModelProperty(required = false,value = "是否回复 1回复，2未回复")
    private Integer replyStatus;
    /**
     * 子订单号
     */
    @ApiModelProperty(required = false,value = "子订单号")
    private String orderSn;
    /**
     * 商品名称
     */
    @ApiModelProperty(required = false,value = "商品名称")
    private String goodsName;
    /**
     * 商品规格
     */
    @ApiModelProperty(required = false,value = "商品规格")
    private List<EsMemberSpecValuesDO> esMemberSpecValuesDO;

    /**
     * 发货速度评分
     */
    @ApiModelProperty(required = false,value = "发货速度评分")
    private Double deliveryScore;
    /**
     * 描述相符度评分
     */
    @ApiModelProperty(required = false,value = "描述相符度评分")
    private Double descriptionScore;
    /**
     * 服务评分
     */
    @ApiModelProperty(required = false,value = "服务评分")
    private Double serviceScore;

}
