package com.jjg.trade.model.dto;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xdl.jjg.model.vo.EsSpecValuesVO;
import com.xdl.jjg.model.vo.TradePromotionGoodsVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: EsCartItemsDTO
 * @Description: 购物车物品
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@Data
@ApiModel(description = "购物车商品")
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemsDTO implements Serializable {

    @ApiModelProperty(value = "")
    private Integer isFailure;
    @ApiModelProperty(value = "上下架状态 2:下架 其他状态为空")
    private Integer marketEnable;

    @ApiModelProperty(value = "卖家id")
    private Long shopId;

    @ApiModelProperty(value = "卖家姓名")
    private String shopName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "产品id")
    private Long skuId;

    @ApiModelProperty(value = "产品sn")
    private String goodsSn;

    @ApiModelProperty(value = "商品所属的分类id")
    private Long categoryId;

    @ApiModelProperty(value = "购买数量")
    private Integer num;

    @ApiModelProperty(value = "商品重量")
    private Double goodsWeight;

    @ApiModelProperty(value = "商品原价")
    private Double goodsPrice;

    @ApiModelProperty(value = "加入购物车价格")
    private Double cartPrice;

    @ApiModelProperty(value = "小计")
    private Double subtotal;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "是否选中，要去结算。0:未选中 1:已选中")
    private Integer checked;

    @ApiModelProperty(value = "是否免运费,1：商家承担运费（免运费） 0：买家承担运费")
    private Integer isFreeFreight;

    @ApiModelProperty(value = "已参与的单品活动工具列表")
    private List<TradePromotionGoodsVO> singleList;

    @ApiModelProperty(value = "已参与的组合活动工具列表")
    private List<TradePromotionGoodsVO> groupList;

    @ApiModelProperty(value = "运费模板id")
    private Integer templateId;

    @ApiModelProperty(value = "规格列表")
    private List<EsSpecValuesVO> specList;

    @ApiModelProperty(value = "积分换购活动中，购买这个商品需要消费的积分")
    private Integer point;

    @ApiModelProperty(name = "last_modify", value = "最后修改时间")
    private Long lastModify;

    @ApiModelProperty(name = "enable_quantity", value = "可用库存")
    private Integer enableQuantity;

    @ApiModelProperty(value = "是否生鲜,1：生鲜 2：非生鲜")
    private Integer isFresh;

    @ApiModelProperty(value = "是否自提,0：否 1：是")
    private Integer isDelivery;

    @ApiModelProperty(value = "配送方式 notInScope 不在配送范围，express 快递，selfMention 自提")
    private String deliveryMethod;

    @ApiModelProperty(value = "运费价格")
    private Double shipPrice;
    @ApiModelProperty(value = "预警值")
    private Integer warningValue;
    @ApiModelProperty(value = "活动类型")
    private String promotionType;
    @ApiModelProperty(value = "该商品参与的所以优惠活动信息")
    private List<TradePromotionGoodsDTO> promotionList;
    @ApiModelProperty(value = "优惠信息")
    private PreferentialMessageDTO preferentialMessage;
    /**
     * Version_2 start
     */
//    @ApiModelProperty(value = "是否使用活动，1 使用，2不适用")
//    private Integer isUsePromotion;
//    @ApiModelProperty(value = "活动类型")
//    private String promotionType;
//    @ApiModelProperty(value = "该商品参与的所以优惠活动信息")
//    private List<TradePromotionGoodsDTO> promotionList;
//    @ApiModelProperty(value = "优惠类型 用于满减活动(不满足优惠：Non,满减：FullMinus，满折:FullDiscount,满免邮：FullFreeShip，满赠品：FullSendGift,满赠券：FullSendBonus)")
//    private String preferentialType;
//    @ApiModelProperty(value = "优惠满足门槛条件")
//    private Double preferentialThreshold;
//    @ApiModelProperty(value = "差额（还差多少钱满足优惠 去凑单）")
//    private Double difference;
//    @ApiModelProperty(value = "优惠价格")
//    private Double preferentialPrice;
//    @ApiModelProperty(value = "优惠折扣")
//    private Integer preferentialDiscount;
//    @ApiModelProperty(value = "满赠品信息")
//    private EsFullDiscountGiftDTO esSendDiscountGift;
//    @ApiModelProperty(value = "满赠券信息")
//    private EsCouponDTO esSendCoupon;

    /**
     * Version_2 end
     */

    /**
     * @param goodsSkuDTO 商品SKU
     * @author: libw 981087977@qq.com
     * @date: 2019/06/12 14:37:28
     * @return:
     */
    public CartItemsDTO(EsGoodsSkuDTO goodsSkuDTO) {
        this.shopId = goodsSkuDTO.getShopId();
        this.shopName = goodsSkuDTO.getShopName();
        this.goodsSn = goodsSkuDTO.getGoodsSn();
        this.goodsId = goodsSkuDTO.getGoodsId();
        this.skuId = goodsSkuDTO.getId();
        this.goodsWeight = goodsSkuDTO.getWeight();
        this.categoryId = goodsSkuDTO.getCategoryId();
        this.goodsPrice = goodsSkuDTO.getMoney();
        this.name = goodsSkuDTO.getGoodsName();
        this.goodsImage = goodsSkuDTO.getThumbnail();
        this.specList = JSON.parseArray(goodsSkuDTO.getSpecs(), EsSpecValuesVO.class);
        this.enableQuantity = goodsSkuDTO.getEnableQuantity();
        this.checked = 0;
        this.isDelivery = 2;
    }

    /**
     * 提供有参构造器是需要提供无参构造
     */
    public CartItemsDTO() {

    }
}
