package com.xdl.jjg.plugin;

import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.dto.SynPaymentParameter;
import com.shopx.trade.api.model.domain.vo.ClientConfigVO;
import com.shopx.trade.api.model.domain.vo.FormVO;
import com.shopx.trade.api.model.domain.vo.PayBillVO;
import com.shopx.trade.api.model.domain.vo.RefundBillVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.TradeType;

import java.util.List;
import java.util.Map;

/**
 * 支付插件
 *
 * @author fk
 * @version v2.0
 * @since v7.0.0
 * 2018-04-11 16:06:57
 */
public interface PaymentPluginManager {

    /**
     * 支付
     *
     * @param bill
     * @return
     */
    FormVO pay(PayBillVO bill);

    /**
     * 同步回调
     *
     * @param tradeType
     */
    DubboResult onReturn(TradeType tradeType, SynPaymentParameter parameter);

    /**
     * 异步回调
     *
     * @param tradeType
     * @param clientType
     * @return
     */
    String onCallback(TradeType tradeType, ClientType clientType, SynPaymentParameter parameter);
    /**
     * 微信异步回调接口，
     *
     * @param tradeType
     * @param clientType
     * @return
     */
    String wxCallback(TradeType tradeType, ClientType clientType, Map<String, String> params);

    /**
     * 主动查询支付结果
     *
     * @param bill
     * @return
     */
    String onQuery(PayBillVO bill);

    /**
     * 获取支付插件id
     *
     * @return
     */
    String getPluginId();

    /**
     * 支付名称
     *
     * @return
     */
    String getPluginName();

    /**
     * 自定义客户端配置文件
     *
     * @return
     */
    List<ClientConfigVO> definitionClientConfig();

    /**
     * 是否支持原路退回   0 不支持  1支持
     *
     * @return
     */
    Integer getIsRetrace();


    /**
     * 退款，原路退回
     *
     * @param bill
     * @return
     */
    boolean onTradeRefund(RefundBillVO bill);

//    /**
//     * 查询退款状态
//     *
//     * @param bill
//     * @return
//     */
//    String queryRefundStatus(RefundBill bill);
}
