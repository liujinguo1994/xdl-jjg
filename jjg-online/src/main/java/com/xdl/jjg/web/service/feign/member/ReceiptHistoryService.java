package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsReceiptHistoryDO;
import com.xdl.jjg.response.service.DubboResult;

public interface ReceiptHistoryService {

    /**
     * 根据发票流水号获取数据
     *
     * @param goodsId 订单编号 goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsIdAndOrdersn(Long goodsId, String orderSn);
}
