package com.jjg.trade.model.form;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 移动端-评论
 * </p>
 *
 * @author rm 2817512105@qq.com
 * @since 2020-02-17 09:28:26
 */
@Data
@ApiModel
public class EsWapCommentForm implements Serializable {


    private static final long serialVersionUID = 3863114069985817704L;
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
    private String original;
    /**
     * 是否匿名
     */
    @ApiModelProperty(required = false,value = "true不匿名，false匿名",example = "true")
    private Boolean isAnonymous;
}
