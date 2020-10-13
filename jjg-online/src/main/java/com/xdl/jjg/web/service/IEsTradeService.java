package com.xdl.jjg.web.service;


import com.xdl.jjg.message.OrderStatusChangeMsg;
import com.xdl.jjg.model.domain.EsBuyerTradeDO;
import com.xdl.jjg.model.domain.EsTradeDO;
import com.xdl.jjg.model.domain.EsWapBalanceTradeDO;
import com.xdl.jjg.model.dto.EsTradeDTO;
import com.xdl.jjg.model.vo.PriceDetailVO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

import java.util.Map;

/**
 * <p>
 * 订单主表-es_trade 服务类
 * </p>
 *
 * @author LiuJG344009799@qq.com
 * @since 2019-05-29
 */
public interface IEsTradeService {

    /**
     * 根据id获取订单主表-es_trade详情
     * @param id
     * @return
     */
    DubboResult<EsTradeDO> getEsTradeInfo(Long id);

    /**
     * 根据id获取订单主表-es_trade详情
     * @param tradeSn
     * @return
     */
    DubboResult<EsTradeDO> getEsTradeInfo(String tradeSn);

    /**
     * 查询订单主表集合分页
     * @param tradeDTO
     * @return
     */
    DubboPageResult<EsTradeDO> getEsTradeInfoList(EsTradeDTO tradeDTO, int pageSize, int pageNum);

    /**
     *保存订单信息
     * @param esTradeDTO
     * @return
     */
    DubboResult<EsTradeDO> insertTrade(EsTradeDTO esTradeDTO);

    /**
     * 通用接口
     * 修改订单详情
     * @param esTradeDTO
     * @return
     */
    DubboResult<EsTradeDO> updateTradeMessage(EsTradeDTO esTradeDTO);

    /**
     * 删除订单
     * @param id
     * @return
     */
    DubboResult<EsTradeDO> deleteTradeMessage(Long id);
    /**
     * liu to li 付款模块时修改订单的付款参数（单独提供）
     * 修改订单状态
     * @param esTradeDTO
     * @return
     */
    DubboResult<EsTradeDO> updateTradeStatus(EsTradeDTO esTradeDTO);
    /**
     * 创建交易
     * @param esTradeDTO
     * @param
     */
    DubboResult insertEsTradeIntoDB(EsTradeDTO esTradeDTO, PriceDetailVO tradePrice);

    /**
     *  发送订单创建消息
     * @param orderStatusChangeMsg
     */
    void sendOrderCreateMessage(OrderStatusChangeMsg orderStatusChangeMsg);

    /**
     * @date20190710
     * LIUJG TO LiBW
     * 根据orderSn获取订单主表-es_trade详情
     * @param orderSn
     * @return
     */
    DubboResult<EsTradeDO> getEsTradeInfoByOrderSn(String orderSn);

    /**
     * 会员余额 操作
     * @author:LIUJG
     * @param tradeSn 交易单号
     * @param orderSn 订单编号
     * @param memberId 会员ID
     * @param orderMoney 要操作的金额
     * @param value 增加或减少
     */
    DubboResult setMemberBalance(String tradeSn, String orderSn, Long memberId, Double orderMoney, String value);

    /**
     * 会员未付款取消交易订单，取消整个交易单
     * @author:LIUJG
     * @param memberId
     * @param tradeSn
     * @return
     */
    DubboResult<EsTradeDO> cancelTrade(Long memberId, String tradeSn);

    DubboPageResult<EsBuyerTradeDO> getEsBuyerOrderList2(EsTradeDTO tradeDTO, int pageSize, int pageNum);

    /**
     * 根据订单编号和退款金额
     * 获取应退的支付金额和余额
     *
     * @param orderSn     订单编号
     * @param refundMoney 退款金额
     * @author: libw 981087977@qq.com
     * @date: 2019/07/30 15:38:12
     * @return: com.shopx.common.model.result.DubboResult<java.util.Map>
     */
    DubboResult<Map> getRefundPrice(String orderSn, Double refundMoney);

    /**
     * 更新退款后的金额
     * @author: libw 981087977@qq.com
     * @date: 2019/07/31 09:13:57
     * @param payMoney          第三方支付金额
     * @param balance           余额支付金额
     * @param refundPayMoney    第三方退款金额
     * @param refundBalance     余额退款金额
     * @param type              类型(0：退款； 1：回滚)
     * @param tradeSn           交易单号
     * @return: com.shopx.common.model.result.DubboResult<java.lang.Boolean>
     */
    DubboResult<Boolean> updateTradeMoney(Double payMoney, Double balance, Double refundPayMoney,
                                          Double refundBalance, Integer type, String tradeSn);

    DubboResult<EsTradeDO> getEsTradeByTradeSn(String tradeSn);


    /**
     * 手机端余额支付更新订单状态
     * @author: yuanj 595831329@qq.com
     * @date: 2020/03/11 09:15:07
     * @param balance          第三方支付金额
     * @return
     */
    DubboPageResult<EsWapBalanceTradeDO> updateOrder(Double balance, String tradeSn);
}
