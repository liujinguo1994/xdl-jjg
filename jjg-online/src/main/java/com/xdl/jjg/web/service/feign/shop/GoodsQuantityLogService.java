package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsQuantityLogDO;
import com.xdl.jjg.response.service.DubboResult;

public interface GoodsQuantityLogService {


    DubboResult<EsGoodsQuantityLogDO> getGoodsQuantityLogByGoodsId(String orderSn, Long skuId);
}
