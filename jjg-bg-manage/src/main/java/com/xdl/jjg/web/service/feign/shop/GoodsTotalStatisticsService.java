package com.xdl.jjg.web.service.feign.shop;

import com.jjg.shop.model.domain.EsGoodsTotalStatisticsDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-shop")
public interface GoodsTotalStatisticsService {

    /**
     * 管理员使用商品总数统计
     * <br/>
     * @return com.shopx.common.model.result.DubboResult<com.shopx.goods.api.model.domain.EsGoodsTotalStatisticsDO>
     * @author KThirty
     * @since 2020/5/27 14:41
     */
    @GetMapping("/adminGoodsTotalStatistics")
    DubboResult<EsGoodsTotalStatisticsDO> adminGoodsTotalStatistics();
}
