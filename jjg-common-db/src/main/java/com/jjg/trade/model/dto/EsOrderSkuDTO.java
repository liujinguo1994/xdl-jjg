package com.jjg.trade.model.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 订单货品项DTO
 * @author Snow create in 2018/5/28
 * @version v2.0
 * @since v7.0.0
 */
@Data
@ApiModel( description = "订单货品项DTO")
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class EsOrderSkuDTO {

    @ApiModelProperty(value = "卖家id")
    private Integer sellerId;

    @ApiModelProperty(value = "卖家姓名")
    private String sellerName;

    @ApiModelProperty(value = "商品id")
    private Integer goodsId;

    @ApiModelProperty(value = "产品id")
    private Integer skuId;

    @ApiModelProperty(value = "产品sn")
    private String skuSn;

    @ApiModelProperty(value = "商品所属的分类id")
    private Integer catId;

    @ApiModelProperty(value = "购买数量")
    private Integer num;

    @ApiModelProperty(value = "商品重量")
    private Double goodsWeight;

    @ApiModelProperty(value = "商品原价")
    private Double originalPrice;

    @ApiModelProperty(value = "购买时的成交价")
    private Double purchasePrice;

    @ApiModelProperty(value = "小计")
    private Double subtotal;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    /** 商品规格列表*/
//    @ApiModelProperty(value = "规格列表")
//    private List<SpecValueVO> specList;

    /** 积分换购活动中，购买这个商品需要消费的积分。 */
    @ApiModelProperty(value = "使用积分")
    private Integer point;

    @ApiModelProperty(value = "快照ID")
    private Integer snapshotId;

    @ApiModelProperty(value = "售后状态")
    private String serviceStatus;

//    @ApiModelProperty(value = "订单操作允许情况" )
//    private GoodsOperateAllowable goodsOperateAllowableVO;

    /** add libw 2019/03/18 添加单品退款功能 start **/
    @ApiModelProperty(value = "订单商品状态")
    private String goodsStatus;

    /**
     * 配送费用
     */
    @ApiModelProperty(name = "shipping_price", value = "配送费用", required = false)
    private Double shippingPrice;

    @ApiModelProperty(value = "普通配送费" )
    private Double  commonFreightPrice;

    @ApiModelProperty(value = "生鲜配送费" )
    private Double  freshFreightPrice;

}
