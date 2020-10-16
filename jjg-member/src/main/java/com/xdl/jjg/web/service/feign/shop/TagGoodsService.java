package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-shop")
public interface TagGoodsService {

    @GetMapping("/getBuyerAdminGoodsTags")
    DubboPageResult<EsGoodsDO> getBuyerAdminGoodsTags(Long shopId, Long tagId, Long type);
}
