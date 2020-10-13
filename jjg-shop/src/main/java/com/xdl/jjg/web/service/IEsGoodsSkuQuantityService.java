package com.xdl.jjg.web.service;


import com.xdl.jjg.model.domain.EsGoodsSkuQuantityDO;
import com.xdl.jjg.model.dto.EsGoodsSkuQuantityDTO;
import com.xdl.jjg.response.service.DubboResult;

import java.util.List;

public interface IEsGoodsSkuQuantityService {

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
