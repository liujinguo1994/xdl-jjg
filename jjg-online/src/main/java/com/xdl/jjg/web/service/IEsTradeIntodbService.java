package com.xdl.jjg.web.service;


import com.jjg.trade.model.domain.EsTradeDO;
import com.jjg.trade.model.dto.EsLfcOrderDTO;

/**
 * 交易入库业务接口
 *
 * @author Snow create in 2018/5/9
 * @version v2.0
 * @since v7.0.0
 */
public interface IEsTradeIntodbService {

    /**
     入库处理
     * @param tradeVO
     */
    void intoDB(EsTradeDO tradeVO, Double balance);

    /**
     * 人寿订单入库处理
     * @param
     */
    void intoLfcOrderDB(EsLfcOrderDTO lfcOrder);

    /**
     * 人寿订单入库处理
     * @param
     */
    void intoXxxOrderDB(EsLfcOrderDTO lfcOrder);



}
