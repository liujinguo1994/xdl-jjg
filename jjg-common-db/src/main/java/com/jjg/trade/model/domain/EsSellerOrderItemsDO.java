package com.jjg.trade.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.jjg.shop.model.domain.EsSpecValuesDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsSellerOrderItemsDO implements Serializable {

    @ApiModelProperty(value = "")
    private Integer isFailure;

    @ApiModelProperty(value = "卖家id")
    private Long shopId;

    @ApiModelProperty(value = "卖家姓名")
    private String shopName;

    @ApiModelProperty(value = "商品id")
    private Long goodsId;

    @ApiModelProperty(value = "产品id")
    private Long skuId;

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

    /**
     * 主键ID
     */
    private Long id;


    /**
     * 已发货数量
     */
    private Integer shipNum;
    /**
     * 订单编号
     */
    private String tradeSn;
    /**
     * 产品sn
     */
    private String goodsSn;
    /**
     * 子订单编号
     */
    private String orderSn;
    /**
     * 图片
     */
    private String image;

    /**
     * 优惠后价格
     */
    private Double money;

    /**
     * 售后状态
     */
    private String state;
    /**
     * 支付快照id
     */
    private Long snapshotId;
    /**
     * 规格json
     */
    private String specJson;
    /**
     * 促销类型
     */
    private String promotionType;
    /**
     * 活动ID
     */
    private Long promotionId;
    /**
     * 发货单号
     */
    private String shipNo;
    /**
     * 签收人
     */
    private String theSign;
    /**
     * 物流公司ID
     */
    private Long logiId;
    /**
     * 物流公司名称
     */
    private String logiName;
    /**
     * 单品评价是否完成
     */
    private String singleCommentStatus;
    /**
     * 发货状态 0 未发货，1 已发货
     */
    private Integer hasShip;
    /**
     * 发票流水号
     */
    private String invoiceNumber;
    /**
     * 商品SKU 信息
     */
    private EsGoodsSkuDO esGoodsSkuDO;

    @ApiModelProperty(value = "开票信息")
    private String invoiceInformation;

    @ApiModelProperty(value = "商品图片")
    private String goodsImage;

    @ApiModelProperty(value = "是否选中，要去结算。0:未选中 1:已选中")
    private Integer checked;

    @ApiModelProperty(value = "是否免运费,1：商家承担运费（免运费） 0：买家承担运费")
    private Integer isFreeFreight;

    @ApiModelProperty(value ="是否生鲜（0生鲜，1非生鲜）")
    private Integer isFresh;

    @ApiModelProperty(value = "规格列表")
    private List<EsSpecValuesDO> specList;
    /**
     * 发票历史信息
     */
    private EsMemberReceiptHistoryDO esMemberReceiptHistoryDO;

    @ApiModelProperty(value = "积分换购活动中，购买这个商品需要消费的积分")
    private Integer point;

    @ApiModelProperty(name = "last_modify", value = "最后修改时间")
    private Long lastModify;

    @ApiModelProperty(name = "enable_quantity", value = "可用库存")
    private Integer enableQuantity;
    private Integer isDelivery;
    @ApiModelProperty(value = "express : 快递，selfMention:自提")
    private String deliveryMethod;
    private Double shipPrice;
    private Integer warningValue;
    /**
     * @param goodsSkuDTO 商品SKU
     * @author: libw 981087977@qq.com
     * @date: 2019/06/12 14:37:28
     * @return:
     */

}
