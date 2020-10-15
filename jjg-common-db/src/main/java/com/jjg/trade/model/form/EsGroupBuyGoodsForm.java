package com.jjg.trade.model.form;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * <p>
 * 团购商品Form
 * </p>
 *
 * @author libw 981087977@qq.com
 * @since 2019-06-04
 */
@Data
@ApiModel
@Accessors(chain = true)
public class EsGroupBuyGoodsForm implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * SKUId
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "SKUId")
    private Long skuId;

    /**
     * 活动Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "活动Id")
    private Long actId;

    /**
     * 团购分类
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "团购分类")
    private Long catId;

    /**
     * 地区Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "地区Id")
    private Long areaId;

    /**
     * 团购名称
     */
    @ApiModelProperty(value = "团购名称")
    private String gbName;

    /**
     * 副标题
     */
    @ApiModelProperty(value = "副标题")
    private String gbTitle;

    /**
     * 商品名称
     */
    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    /**
     * 商品Id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商品Id")
    private Long goodsId;

    /**
     * 原始价格
     */
    @ApiModelProperty(value = "原始价格")
    private Double originalPrice;

    /**
     * 团购价格
     */
    @ApiModelProperty(value = "团购价格")
    private Double price;

    /**
     * 图片地址
     */
    @ApiModelProperty(value = "图片地址")
    private String imgUrl;

    /**
     * 商品总数
     */
    @ApiModelProperty(value = "商品总数")
    private Integer goodsNum;

    /**
     * 虚拟数量
     */
    @ApiModelProperty(value = "虚拟数量")
    private Integer visualNum;

    /**
     * 限购数量
     */
    @ApiModelProperty(value = "限购数量")
    private Integer limitNum;

    /**
     * 已团购数量
     */
    @ApiModelProperty(value = "已团购数量")
    private Integer buyNum;

    /**
     * 浏览数量
     */
    @ApiModelProperty(value = "浏览数量")
    private Integer viewNum;

    /**
     * 介绍
     */
    @ApiModelProperty(value = "介绍")
    private String remark;

    /**
     * 状态
     */
    @ApiModelProperty(value = "状态")
    private Integer gbStatus;

    /**
     * 添加时间
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "添加时间")
    private Long addTime;

    /**
     * wap缩略图
     */
    @ApiModelProperty(value = "wap缩略图")
    private String wapThumbnail;

    /**
     * pc缩略图
     */
    @ApiModelProperty(value = "pc缩略图")
    private String thumbnail;

    /**
     * 商家ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @ApiModelProperty(value = "商家ID")
    private Long sellerId;

    /**
     * 店铺名称
     */
    @ApiModelProperty(value = "店铺名称")
    private String sellerName;

}
