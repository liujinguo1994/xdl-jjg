package com.jjg.member.model.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员店铺收藏
 * </p>
 *
 * @author lins 1220316142@qq.com
 * @since 2019-05-29
 */
@Data
public class EsCollectionShopVO implements Serializable {


    /**
     * 收藏商品ID
     */
    @ApiModelProperty(required = false,value = "收藏商品ID",example = "1")
    private Long goodsId;
    /**
     * 商品名称
     */
    @ApiModelProperty(required = false,value = "商品名称")
    private String goodsName;
    /**
     * 商品价格
     */
    @ApiModelProperty(required = false,value = "商品价格",example = "120.3")
    private Double money;
    /**
     * 商品编号
     */
    @ApiModelProperty(required = false,value ="商品编号")
    private String goodsSn;
    /**
     * 商品图片
     */
    @ApiModelProperty(required = false,value = "商品图片")
    private String goodsImg;
    /**
     * 可用库存=SKU之和
     */
    @ApiModelProperty(required = false,value = "库存",example = "1")
    private Integer quantity;
    /**
     * 详情
     */
    @ApiModelProperty(required = false,value = "详情")
    private String intro;
    /**
     * 原图路径
     */
    @ApiModelProperty(required = false,value = "原图路径")
    private String original;
    /**
     * 销量
     */
    @ApiModelProperty(required = false,value = "销量")
    private Integer buyCount;

}
