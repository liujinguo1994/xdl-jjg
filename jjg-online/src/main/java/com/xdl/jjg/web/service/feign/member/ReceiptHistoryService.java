package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsReceiptHistoryDO;
import com.jjg.member.model.dto.EsReceiptHistoryDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-member")
public interface ReceiptHistoryService {


    /**
     * 插入数据
     *
     * @param receiptHistoryDTO 发票历史DTO
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:39:30
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */

    DubboResult insertReceiptHistory(@RequestBody EsReceiptHistoryDTO receiptHistoryDTO);

    /**
     * 根据发票流水号获取数据
     *
     * @param goodsId 订单编号 goodsId
     * @auther: lins 1220316142@qq.com
     * @date: 2019/05/31 16:37:16
     * @return: com.shopx.common.model.result.DubboResult<EsReceiptHistoryDO>
     */
    DubboResult<EsReceiptHistoryDO> getReceiptHistoryByGoodsIdAndOrdersn(@RequestParam("goodsId") Long goodsId, @RequestParam("orderSn") String orderSn);
}
