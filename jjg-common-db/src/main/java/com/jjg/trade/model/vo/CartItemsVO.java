package com.jjg.trade.model.vo;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.vo.EsSpecValuesVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName: EsCartItemsVO
 * @Description: 购物车物品
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@Data
@ApiModel(description = "购物车商品")
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartItemsVO implements Serializable {

    @ApiModelProperty(value = "是否失效 , 1未失效 2失效 购物车用来判断失效状态")
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

    @ApiModelProperty(value = "加入购物车价格(购买时成交价)")
    private Double cartPrice;

    @ApiModelProperty(value = "小计")
    private Double subtotal;

    @ApiModelProperty(value = "商品名称")
    private String name;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "是否选中，要去结算。0:未选中 1:已选中")
    private Integer checked;

    @ApiModelProperty(value = "是否免运费,2：商家承担运费（免运费） 1：买家承担运费")
    private Integer isFreeFreight;

    @ApiModelProperty(value = "已参与的单品活动工具列表")
    private List<TradePromotionGoodsVO> singleList;

    @ApiModelProperty(value = "已参与的组合活动工具列表")
    private List<TradePromotionGoodsVO> groupList;

    @ApiModelProperty(value = "运费模板id")
    private Long templateId;

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

    @ApiModelProperty(value = "是否自提,1：是 2：否 ")
    private Integer isDelivery;

    @ApiModelProperty(value = "配送方式 notInScope 不在配送范围，express 快递，selfMention 自提")
    private String deliveryMethod = "express";

    @ApiModelProperty(value = "运费价格")
    private Double shipPrice;
    @ApiModelProperty(value = "预警值")
    private Integer warningValue;

    @ApiModelProperty(value = "该商品优惠后的总价")
    private Double thisGoodsPrice;
    /**
     * Version_2 start
     */
    @ApiModelProperty(value = "活动类型")
    private String promotionType;
    @ApiModelProperty(value = "该商品参与的所有优惠活动信息")
    private List<TradePromotionGoodsVO> promotionList;
    @ApiModelProperty(value = "优惠信息")
    private PreferentialMessageVO preferentialMessage;

    @ApiModelProperty(value = "手机端是否自提,1：是 2：否 ")
    private Integer isSelf;
    @ApiModelProperty(value = "购物车该商品是否被收藏,1：是 2：否 ")
    private Integer isCollection;



    /**
     * Version_2 end
     */

    /**
     * @param goodsSkuCO 商品SKU
     * @author: libw 981087977@qq.com
     * @date: 2019/06/12 14:37:28
     * @return:
     */
    public CartItemsVO(EsGoodsSkuCO goodsSkuCO) {
        this.shopId = goodsSkuCO.getShopId();
        this.shopName = goodsSkuCO.getShopName();
        this.goodsSn = goodsSkuCO.getGoodsSn();
        this.goodsId = goodsSkuCO.getGoodsId();
        this.skuId = goodsSkuCO.getId();
        this.goodsWeight = goodsSkuCO.getWeight();
        this.categoryId = goodsSkuCO.getCategoryId();
        this.cartPrice = goodsSkuCO.getMoney();
        this.goodsPrice = goodsSkuCO.getMoney();
        this.name = goodsSkuCO.getGoodsName();
        this.specList = JSON.parseArray(goodsSkuCO.getSpecs(), EsSpecValuesVO.class);
        this.enableQuantity = goodsSkuCO.getEnableQuantity();
        this.warningValue = goodsSkuCO.getWarningValue() == null ? 10 : goodsSkuCO.getWarningValue();
        this.isDelivery = 2;

        // V_2 start
        this.isCollection = 2;
        this.promotionType = "NO";
        this.preferentialMessage = new PreferentialMessageVO();
        // V_2 end
    }

    public CartItemsVO() {

    }
}
