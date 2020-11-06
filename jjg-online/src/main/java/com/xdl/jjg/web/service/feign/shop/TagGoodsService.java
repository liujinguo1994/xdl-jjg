package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface TagGoodsService {



    @GetMapping("/queryTagGoods")
    DubboPageResult<EsGoodsDO> queryTagGoods(@RequestParam("shopId") Integer shopId, @RequestParam("num") Integer num,@RequestParam("mark")  String mark);



}
