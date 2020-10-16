package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.co.EsGoodsSkuCO;
import com.jjg.shop.model.domain.EsGoodsSkuDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "jjg-shop")
public interface GoodsSkuService {

    /**
     * 买家端 根据商品SKU编号获取SKU信息，先判断商品是否存在
     * @param skuId
     * @param goodsId
     * @return
     */
    @GetMapping("/buyGetGoodsSkuBySkuIdAndGoodsId")
    DubboResult<EsGoodsSkuDO> buyGetGoodsSku(@RequestParam("skuId") Long skuId, @RequestParam("goodsId") Long goodsId);

    /**
     * 根据id获取数据
     */
    @GetMapping("/getGoodsSkuById")
    DubboResult<EsGoodsSkuCO> getGoodsSku(@RequestParam("id") Long id);


}
