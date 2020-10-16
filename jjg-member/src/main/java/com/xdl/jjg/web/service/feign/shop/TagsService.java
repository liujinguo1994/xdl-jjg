package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsTagsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-shop")
public interface TagsService {

    /**
     * 获取商家标签列表
     * @param shopId
     * @return
     */
    @GetMapping("/getTagsList")
    DubboPageResult<EsTagsDO> getTagsList(Long shopId);

}
