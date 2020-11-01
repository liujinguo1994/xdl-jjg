package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsSkuQuantityDO;
import com.jjg.shop.model.dto.EsGoodsSkuQuantityDTO;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

public interface GoodsSkuQuantityService {

    /**
     * 库存增加
     * @param quantityDTO 库存DTO
     * @return
     */
    DubboResult<EsGoodsSkuQuantityDO> insertGoodsSkuQuantity(List<EsGoodsSkuQuantityDTO> quantityDTO);


    /**
     * 库存扣减
     * @return
     */
    DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantity(List<EsGoodsSkuQuantityDTO> quantityDTO);

    DubboResult<EsGoodsSkuQuantityDO> reduceGoodsSkuQuantityRedis(List<EsGoodsSkuQuantityDTO> quantityDTO);
}
