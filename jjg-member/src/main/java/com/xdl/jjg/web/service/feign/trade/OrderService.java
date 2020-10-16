package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsOrderDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-online")
public interface OrderService {

    /**
     * 买家端
     * 查询订单明细表
     * 会员判断是够已经下过订单
     * @param orderSn
     * @return EsOrderDO
     */
    @GetMapping("/getEsBuyerOrderInfoByOrderSn")
    DubboResult<EsOrderDO> getEsBuyerOrderInfo(String orderSn);

}
