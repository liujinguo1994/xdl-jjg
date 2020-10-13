package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.shopx.common.util.JsonUtil;
import com.shopx.trade.api.model.domain.EsGoodsSkuAndLfcDO;
import com.shopx.trade.api.model.domain.dto.EsLfcOrderDTO;
import com.shopx.trade.api.model.enums.*;
import com.shopx.trade.api.utils.CurrencyUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 订单明细表-es_order
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_order")
public class EsOrder extends Model<EsOrder> {

    private static final long serialVersionUID = 1L;

    /**
     * 订单主键
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 父订单编号
     */
    @TableField("trade_sn")
    private String tradeSn;
    /**
     * 子订单订单编号
     */
    @TableField("order_sn")
    private String orderSn;
    /**
     * 店铺ID
     */
    @TableField("shop_id")
    private Long shopId;
    /**
     * 店铺名称
     */
    @TableField("shop_name")
    private String shopName;
    /**
     * 会员ID
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 买家姓名
     */
    @TableField("member_name")
    private String memberName;
    /**
     * 订单状态
     */
    @TableField("order_state")
    private String orderState;
    /**
     * 付款状态
     */
    @TableField("pay_state")
    private String payState;
    /**
     * 货运状态
     */
    @TableField("ship_state")
    private String shipState;
    /**
     * 售后状态
     */
    @TableField("service_state")
    private String serviceState;

    /**
     * 售后类型
     */
    @TableField("service_type")
    private String serviceType;
    /**
     * 结算状态
     */
    @TableField("bill_state")
    private String billState;
    /**
     * 评论是否完成
     */
    @TableField("comment_status")
    private String commentStatus;
    /**
     * 支付方式id
     */
    @TableField("payment_method_id")
    private Long paymentMethodId;
    /**
     * 支付方式名称
     */
    @TableField("payment_method_name")
    private String paymentMethodName;
    /**
     * 支付方式类型
     */
    @TableField("payment_type")
    private String paymentType;
    /**
     * 支付插件名称
     */
    @TableField("plugin_id")
    private String pluginId;
    /**
     * 支付时间
     */
    @TableField("payment_time")
    private Long paymentTime;
    /**
     * 支付金额
     */
    @TableField("pay_money")
    private Double payMoney;
    /**
     * 订单总额
     */
    @TableField("order_money")
    private Double orderMoney;
    /**
     * 余额支付
     */
    @TableField("payed_money")
    private Double payedMoney;
    /**
     * 商品总额(折扣后价格)
     */
    @TableField("goods_money")
    private Double goodsMoney;
    /**
     * 优惠金额
     */
    @TableField("discount_money")
    private Double discountMoney;
    /**
     * 配送费用
     */
    @TableField("shipping_money")
    private Double shippingMoney;
    /**
     * 普通配送费用
     */
    @TableField(value = "common_freight_price" )
    private Double  commonFreightPrice;
    /**
     * 生鲜配送费
     */
    @TableField(value = "fresh_freight_price" )
    private Double  freshFreightPrice;
    /**
     * 0没退,1已经退了
     */
    @TableField(value = "has_fresh" )
    private Integer  hasFresh;
    /**
     * 0没退,1已经退了
     */
    @TableField(value = "has_comm")
    private Integer hasComm;

    /**
     * 需要第三方支付的金额
     */
    @TableField("need_pay_money")
    private Double needPayMoney;
    /**
     * 收货人姓名
     */
    @TableField("ship_name")
    private String shipName;
    /**
     * 收货地址
     */
    @TableField("ship_addr")
    private String shipAddr;
    /**
     * 收货人邮编
     */
    @TableField("ship_zip")
    private String shipZip;
    /**
     * 收货人手机
     */
    @TableField("ship_mobile")
    private String shipMobile;
    /**
     * 收货人电话
     */
    @TableField("ship_tel")
    private String shipTel;
    /**
     * 配送方式（快递 自提）
     */
    @TableField("ship_Method")
    private String shipMethod;
    /**
     * 收货时间
     */
    @TableField("receive_time")
    private String receiveTime;
    /**
     * 配送地区-省份ID
     */
    @TableField("ship_province_id")
    private Long shipProvinceId;
    /**
     * 配送地区-城市ID
     */
    @TableField("ship_city_id")
    private Long shipCityId;
    /**
     * 配送地区-区(县)ID
     */
    @TableField("ship_county_id")
    private Long shipCountyId;
    /**
     * 配送街道id
     */
    @TableField("ship_town_id")
    private Long shipTownId;
    /**
     * 配送地区-省份
     */
    @TableField("ship_province")
    private String shipProvince;
    /**
     * 配送地区-城市
     */
    @TableField("ship_city")
    private String shipCity;
    /**
     * 配送地区-区(县)
     */
    @TableField("ship_county")
    private String shipCounty;
    /**
     * 配送街道
     */
    @TableField("ship_town")
    private String shipTown;
    /**
     * 是否被删除 0 未删除 1删除
     */
    @TableField("is_del")
    private Integer isDel;
    /**
     * 商品数量
     */
    @TableField("goods_num")
    private Integer goodsNum;
    /**
     * 订单备注
     */
    private String remark;
    /**
     * 订单取消原因
     */
    @TableField("cancel_reason")
    private String cancelReason;
    /**
     * 货物列表json
     */
    @TableField("items_json")
    private String itemsJson;
    /**
     * 会员收货地址ID
     */
    @TableField("address_id")
    private Long addressId;
    /**
     * 管理员备注
     */
    @TableField("admin_remark")
    private String adminRemark;
    /**
     * 完成时间
     */
    @TableField("complete_time")
    private Long completeTime;
    /**
     * 确认收货签收时间
     */
    @TableField("signing_time")
    private Long signingTime;
    /**
     * 发货时间
     */
    @TableField("ship_time")
    private Long shipTime;
    /**
     * 支付返回的交易号
     */
    @TableField("pay_order_no")
    private String payOrderNo;
    /**
     * 订单来源 (pc、wap、app)
     */
    @TableField("client_type")
    private String clientType;
    /**
     * 买家账号
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 是否需要发票,0：否，1：是
     */
    @TableField("need_receipt")
    private Integer needReceipt;

    /**
     * 签约公司
     */
    @TableField("company_id")
    private Long companyId;
    /**
     * 订单创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 订单更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Long updateTime;

    /**
     * 自提信息文本
     */
    @TableField(exist = false)
    private String deliveryText;

    /**
     * 自提时间
     */
    @TableField(exist = false)
    private String deliveryTime;
    /**
     * 取消订单时间
     */
    @TableField(value = "cancel_time")
    private Long cancelTime;

    /**
     * 申请退款时间
     */
    @TableField(value = "apply_refund_time")
    private Long applyRefundTime;
    @TableField(value = "lfc_id")
    private String lfcId;
    /**
     * 商品模块查询使用
     * 商品名称
     */
//    @TableField(exist = false)
//    private String goodsName;
    public EsOrder(){

    }
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
    public EsOrder(EsLfcOrderDTO lfcOrder, List<EsGoodsSkuAndLfcDO> goodsSkuDOList ) {

        this.receiveTime = "任意时间";
        //this.shipTime = orderDTO.getShipTime();
        this.paymentType = PaymentTypeEnum.ONLINE.name();

        // 收货人
        //ConsigneeVO consignee = orderDTO.getConsignee();
        shipName = lfcOrder.getReceiverName();
        shipAddr = lfcOrder.getReceiverAddressDetail();

        this.addressId = 510L;
        this.shipMobile = lfcOrder.getReceiverPhone();

        this.shipProvince = lfcOrder.getReceiverProvinceName();
        this.shipCity = lfcOrder.getReceiverCityName();
        this.shipCounty = lfcOrder.getReceiverDistrictName();

        this.shippingMoney = 0.0;
        this.discountMoney = 0.0;

        this.payedMoney=0.0;//已付金额为0，后面使用余额补上去
        // 卖家
      /*  this.shopId = 38L;
        this.shopName = "卓付商城";
        // 买家
        this.memberId = 510L;
        this.memberName = "xxxOrder";*/
        // 初始化状态
        this.shipState = ShipStatusEnum.SHIP_NO.value();
        this.orderState = OrderStatusEnum.NEW.value();
        this.payState = PayStatusEnum.PAY_NO.value();
        this.commentStatus = CommentStatusEnum.UNFINISHED.value();
        this.serviceState = ServiceStatusEnum.NOT_APPLY.value();
        this.isDel = 0;
        List<EsOrderItems> orderSkuVOList = new ArrayList<>();
        for (EsGoodsSkuAndLfcDO skuDO:goodsSkuDOList) {
            EsOrderItems orderSkuVO = new EsOrderItems();
            orderSkuVO.setGoodsId(skuDO.getGoodsId());
            orderSkuVO.setSkuId(skuDO.getSkuId());
            orderSkuVO.setNum(skuDO.getSaleCount());
            orderSkuVO.setState(OrderStatusEnum.CONFIRM.value());
            orderSkuVO.setMoney(CurrencyUtil.mul(skuDO.getMorey(),skuDO.getSaleCount()));
            orderSkuVO.setName(skuDO.getGoodsName());
            orderSkuVO.setImage(skuDO.getThumbnail());
            orderSkuVOList.add(orderSkuVO);
        }
        // 产品列表
        this.itemsJson = JsonUtil.objectToJson(orderSkuVOList);
        //发票
        this.needReceipt = 0;
        //订单来源
        this.clientType = "PC";
    }


}
