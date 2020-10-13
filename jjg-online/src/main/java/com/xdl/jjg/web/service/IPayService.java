package com.xdl.jjg.web.service;

import com.shopx.common.model.result.DubboResult;
import com.shopx.trade.api.model.domain.dto.SynPaymentParameter;
import com.shopx.trade.api.model.domain.vo.FormVO;
import com.shopx.trade.api.model.domain.vo.PayBillVO;
import com.shopx.trade.api.model.enums.ClientType;
import com.shopx.trade.api.model.enums.TradeType;

import java.util.Map;

/**
 * @ClassName: IPayService
 * @Description: 支付接口
 * @Author: libw  981087977@qq.com
 * @Date: 7/9/2019 18:51
 * @Version: 1.0
 */
public interface IPayService {

    /**
     * 支付
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:56:29
     * @param bill  交易信息
     * @return: com.shopx.common.model.result.DubboResult<com.shopx.trade.api.model.domain.vo.FormVO>
     */
    DubboResult<FormVO> pay(PayBillVO bill);

    /**
     * 支付宝同步回调接口，
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:56:55
     * @param tradeType 交易类型
     * @return: com.shopx.common.model.result.DubboResult
     */
    DubboResult onReturn(TradeType tradeType, String paymentPluginId, SynPaymentParameter parameter);

    /**
     * 支付宝异步回调接口
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:57:13
     * @param tradeType     交易类型
     * @param clientType    客户端类型
     * @return: java.lang.String
     */
    DubboResult onCallback(TradeType tradeType, String paymentPluginId, ClientType clientType, SynPaymentParameter parameter);
    /**
     * 微信异步回调接口，
     *
     * @param tradeType
     * @param paymentPluginId
     * @param clientType
     * @param params
     * @return
     */
    DubboResult wxCallback(TradeType tradeType, String paymentPluginId, ClientType clientType, Map<String, String> params);

    /**
     * 主动查询支付结果
     * @author: libw 981087977@qq.com
     * @date: 2019/07/09 18:59:50
     * @param bill  交易单信息
     * @return: DubboResult
     */
    String onQuery(PayBillVO bill);

    /**
     * 原路退回
     *
     * @param returnTradeNo 第三方订单号
     * @param refundSn      退款编号
     * @param refundPrice   退款金额
     * @return
     */
    DubboResult<Map> originRefund(String returnTradeNo, String refundSn, Double refundPrice);
}