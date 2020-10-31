package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsWordsDO;
import com.xdl.jjg.response.service.DubboPageResult;

public interface GoodsWordsService {




    /**
     * 首页联想
     * @param keyword
     * @return
     */
    DubboPageResult<EsGoodsWordsDO> getGoodsWords(String keyword);
}
