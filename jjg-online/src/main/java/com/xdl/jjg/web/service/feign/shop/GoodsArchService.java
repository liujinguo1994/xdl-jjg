package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsArchDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface GoodsArchService {

    /**
     * 根据商品id查询赠品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsArchDO> getGoodsArchGifts(@RequestParam("id") Long id);
}
