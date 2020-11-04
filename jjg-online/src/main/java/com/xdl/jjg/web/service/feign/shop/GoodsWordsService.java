package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsWordsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface GoodsWordsService {




    /**
     * 首页联想
     * @param keyword
     * @return
     */
    DubboPageResult<EsGoodsWordsDO> getGoodsWords(@RequestParam("keyword") String keyword);
}
