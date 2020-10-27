package com.xdl.jjg.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jjg.trade.model.domain.EsLfcOrderSkuDO;
import com.jjg.trade.model.enums.PaymentTypeEnum;
import com.jjg.trade.model.enums.TradeStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 订单主表-es_trade
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
@Data
@TableName("es_trade")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EsTrade implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value="id", type= IdType.AUTO)
    private Long id;
    /**
     * 订单编号
     */
    @TableField("trade_sn")
    private String tradeSn;
    /**
     * 买家id
     */
    @TableField("member_id")
    private Long memberId;
    /**
     * 买家用户名
     */
    @TableField("member_name")
    private String memberName;
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
     * 总价格
     */
    @TableField("total_money")
    private Double totalMoney;
    /**
     * 商品价格
     */
    @TableField("goods_money")
    private Double goodsMoney;
    /**
     * 运费
     */
    @TableField("freight_money")
    private Double freightMoney;
    /**
     * 优惠的金额
     */
    @TableField("discount_money")
    private Double discountMoney;

    /**
     * 余额支付（如果使用余额小于交易总额，则不对订单进行余额付款）
     */
    @TableField("use_balance")
    private Double useBalance;

    /**
     * 支付金额
     */
    @TableField("pay_money")
    private Double payMoney;

    /**
     * 收货地址id
     */
    @TableField("consignee_id")
    private Long consigneeId;
    /**
     * 收货人姓名
     */
    @TableField("consignee_name")
    private String consigneeName;
    /**
     * 收货国家
     */
    @TableField("consignee_country")
    private String consigneeCountry;
    /**
     * 收货国家id
     */
    @TableField("consignee_country_id")
    private Long consigneeCountryId;
    /**
     * 收货省
     */
    @TableField("consignee_province")
    private String consigneeProvince;
    /**
     * 收货省id
     */
    @TableField("consignee_province_id")
    private Long consigneeProvinceId;
    /**
     * 收货市
     */
    @TableField("consignee_city")
    private String consigneeCity;
    /**
     * 收货市id
     */
    @TableField("consignee_city_id")
    private Long consigneeCityId;
    /**
     * 收货区
     */
    @TableField("consignee_county")
    private String consigneeCounty;
    /**
     * 收货区id
     */
    @TableField("consignee_county_id")
    private Long consigneeCountyId;
    /**
     * 收货镇
     */
    @TableField("consignee_town")
    private String consigneeTown;
    /**
     * 收货镇id
     */
    @TableField("consignee_town_id")
    private Long consigneeTownId;
    /**
     * 收货详细地址
     */
    @TableField("consignee_address")
    private String consigneeAddress;
    /**
     * 收货人手机号
     */
    @TableField("consignee_mobile")
    private String consigneeMobile;
    /**
     * 收货人电话
     */
    @TableField("consignee_telephone")
    private String consigneeTelephone;
    /**
     * 交易创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Long createTime;
    /**
     * 订单状态
     */
    @TableField("trade_status")
    private String tradeStatus;
    /**
     * 是否被删除 0 未删除 1删除
     */
    @TableField("is_del")
    private Integer isDel;

    /**
     * 使用余额
     */
    @TableField(exist = false)
    private Double balance;
    
    /**
     * 配送方式（快递 自提）
     */
    @TableField("ship_Method")
    private String shipMethod;

    /**
     * 取消订单时间
     */
    @TableField(value = "cancel_time")
    private Long cancelTime;
    @TableField(value = "has_balance")
    private Integer hasBalance;
    /**人寿订单号*/
    @TableField(value = "lfc_id")
    private String lfcId;
    @TableField(value = "is_deposit")
    private Integer isDeposit;
    public EsTrade(List<EsLfcOrderSkuDO> orderSkuList) {

        this.setFreightMoney(0.0);
        this.setDiscountMoney(0.0);
        this.setPaymentType(PaymentTypeEnum.ONLINE.name());
        //交易状态
        this.setTradeStatus(TradeStatusEnum.NEW.value());
        this.setMemberId(401L);//人寿账号
        this.setMemberName("lfcOrder");
    }
    public EsTrade(){}
    protected Serializable pkVal() {
        return this.id;
    }

}