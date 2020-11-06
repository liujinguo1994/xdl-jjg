package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsQuantityLogDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface GoodsQuantityLogService {


    @GetMapping("/getGoodsQuantityLogByGoodsId")
    DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLogByGoodsId(@RequestParam("orderSn") String orderSn, @RequestParam("skuId") Long skuId);
}
