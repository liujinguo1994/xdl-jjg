package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface GoodsService {

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    @GetMapping("/getGoodsById")
    DubboResult<EsGoodsCO> getEsGoods(@RequestParam(value = "id") Long id);
}
