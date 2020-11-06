package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsReturnReasonDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface ReturnReasonService {

    /**
     * 根据售后类型获取原因列表
     * 售后类型(RETURN_MONEY:退款,RETURN_GOODS:退货退款,CHANGE_GOODS:换货,REPAIR_GOODS:维修)
     */
    @GetMapping("/getByType")
    DubboPageResult<EsReturnReasonDO> getByType(@RequestParam("refundType") String refundType);
}
