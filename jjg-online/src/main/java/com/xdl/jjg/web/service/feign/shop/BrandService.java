package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsBrandDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-shop")
public interface BrandService {

    DubboPageResult<EsBrandDO> getBrandsByCategory(@RequestParam("categoryId") Long categoryId);
}
