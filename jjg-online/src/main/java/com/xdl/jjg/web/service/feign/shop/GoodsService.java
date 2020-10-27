package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsCO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-shop")
public interface GoodsService {


    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsGoods(Long id);

    /**
     * 根据商品ID获取商品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsCO> getEsBuyerGoods(Long id);

}
