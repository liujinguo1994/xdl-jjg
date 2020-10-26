package com.jjg.trade.model.vo;

import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.RequestParam;
import com.shopx.trade.api.model.enums.TradeType;
import lombok.Data;
import java.io.Serializable;
import java.util.Map;

/**
 * 支付的订单信息
 * @Author: libw  981087977@qq.com
 * @Date: 6/11/2019 15:37
 * @Version: 1.0
 */
@Data
public class PayBillVO implements Serializable {

    /**
     * 编号
     */
    private String outTradeNo;

    /**
     * 要支付的金额
     */
    private Double orderPrice;

    /**
     * normal:正常的网页跳转
     * qr:二维码扫描
     */
    private String payMode;

    /**
     * 交易类型
     */
    private TradeType tradeType;

    /**
     * 支付类型
     */
    private ClientType clientType;

    /**
     * 支付插件id
     */
    private String paymentPluginId;

    /**
     * 支付插件配置信息
     */
    private String config;

    /**
     * 请求传来的一些支付要用到的处理信息
     *
     * @see RequestParam
     */
    private Map<String, String> requestInfo;

    /**
     * 用户唯一标识
     */
    private String openid;

    public PayBillVO(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public PayBillVO() {
    }
}
