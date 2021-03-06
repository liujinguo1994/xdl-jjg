package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsSkuQuantityDO;
import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
@FeignClient(value = "jjg-shop")
public interface GoodsSkuQuantityService {

    /**
     * 库存增加
     * @param quantityDTO 库存DTO
     * @return
     */
    @PostMapping("/insertGoodsSkuQuantity")
    DubboResult<EsGoodsSkuQuantityDO> insertGoodsSkuQuantity(@RequestBody List<EsGoodsSkuQuantityDTO> quantityDTO);


    /**
     * 库存扣减
     * @return
     */
    @PostMapping("/reduceGoodsSkuQuantity")
    DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantity(@RequestBody List<EsGoodsSkuQuantityDTO> quantityDTO);
    @PostMapping("/reduceGoodsSkuQuantityRedis")
    DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantityRedis(@RequestBody List<EsGoodsSkuQuantityDTO> quantityDTO);
}
