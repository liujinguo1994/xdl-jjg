package com.xdl.jjg.web.service.feign.shop;

import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "jjg-shop")
public interface GoodsParamsService {
}
