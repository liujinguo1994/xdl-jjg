package com.jjg.member.model.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 会员商品收藏
 * </p>
 *
 * @author HQL 236154186@qq.com
 * @since 2019-05-29
 */
@Data
@Api
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsMemberCollectionGoodsVO implements Serializable {


    /**
     * 主键ID
     */
    @ApiModelProperty(required = false,value = "主键ID",example = "1")
	private Long id;
    /**
     * 会员ID
     */
    @ApiModelProperty(required = false,value = "会员ID",example = "1" )
	private Long memberId;
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
    @ApiModelProperty(required = false,value = "商品价格",example = "1.0")
	private Double  goodsPrice;
    /**
     * 商品编号
     */
    @ApiModelProperty(required = false,value = "商品编号")
	private String goodsSn;
    /**
     * 商品图片
     */
    @ApiModelProperty(required = false,value = "商品图片")
	private String goodsImg;
    /**
     * 店铺id
     */
    @ApiModelProperty(required = false,value = "店铺id")
	private Long shopId;
    /**
     * 降价提醒 1不提醒，2提醒
     */
    @ApiModelProperty(required = false,value = "降价提醒 1不提醒，2提醒",example = "1")
    private Integer priceRemind;
    /**
     * 剩余库存
     */
    @ApiModelProperty(required = false,value = "剩余库存",example = "1")
    private Integer quantity;
    /**
     * 库存紧缺
     */
    @ApiModelProperty(required = false,value = "库存紧缺")
    private String shortQuantity;
    /**
     * 降价金额
     */
    @ApiModelProperty(required = false,value = "降价金额")
    private Double cutMoney;
    /**
     * skuId
     */
    @ApiModelProperty(required = false,value = "skuId")
    private Long skuId;
    /**
     * 商品现价
     */
    @ApiModelProperty(required = false,value = "商品现价")
    private Double currentPrice;
    /**
     *  2失效
     */
    @ApiModelProperty(required = false,value = "2失效")
    private Integer loseSign;
    /**
     * 商品分类id
     */
    @ApiModelProperty(required = false,value = "商品分类id")
    private Long categoryId;

}
