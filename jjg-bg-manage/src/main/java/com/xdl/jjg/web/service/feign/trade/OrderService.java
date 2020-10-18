package com.xdl.jjg.web.service.feign.trade;

import com.jjg.trade.model.domain.EsOrderDO;
import com.jjg.trade.model.domain.EsSellerOrderDO;
import com.jjg.trade.model.dto.EsAdminOrderQueryDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-online")
public interface OrderService {

    /**
     * 系统后台订单列表
     * @param esAdminOrderQueryDTO
     * @param pageSize
     * @param pageNum
     * @return
     */
    @GetMapping("/getEsAdminOrderList")
    DubboPageResult<EsOrderDO> getEsAdminOrderList(@RequestBody EsAdminOrderQueryDTO esAdminOrderQueryDTO, @RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
    /**
     * 系统后台
     * 查询订单明细信息
     * @param
     * @return EsOrderDO
     */
    @GetMapping("/getEsAdminOrderInfo")
    DubboResult<EsSellerOrderDO> getEsAdminOrderInfo(@RequestParam("orderSn") String orderSn);
}
