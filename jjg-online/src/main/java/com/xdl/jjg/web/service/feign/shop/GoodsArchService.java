package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsArchDO;
import com.xdl.jjg.response.service.DubboResult;

public interface GoodsArchService {

    /**
     * 根据商品id查询赠品信息
     * @param id
     * @return
     */
    DubboResult<EsGoodsArchDO> getGoodsArchGifts(Long id);
}
