package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;

public interface TagGoodsService {



    DubboPageResult<EsGoodsDO> queryTagGoods(Integer shopId, Integer num, String mark);



}
