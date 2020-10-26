package com.xdl.jjg.manager.event;


import com.jjg.trade.model.dto.EsTradeDTO;
import com.jjg.trade.model.vo.EsTradeSnMoneyVO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 交易入库事件
 * @author Snow create in 2018/6/26
 * @version v2.0
 * @since v7.0.0
 */
public interface TradeIntoDbEvent {


    /**
     * 交易入库
     * @param esTradeDTO
     */
    DubboResult<EsTradeSnMoneyVO> onTradeIntoDb(EsTradeDTO esTradeDTO) /*throws MQClientException*/;

    DubboResult<EsTradeSnMoneyVO> onTradeIntoDbYC(EsTradeDTO esTradeDTO) /*throws MQClientException*/;
}
