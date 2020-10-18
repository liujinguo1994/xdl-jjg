package com.xdl.jjg.web.service.feign.member;

import com.jjg.member.model.domain.EsGrowthValueStrategyDO;
import com.jjg.member.model.dto.EsGrowthStrategyDTO;
import com.xdl.jjg.response.service.DubboPageResult;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "jjg-member")
public interface GrowthValueStrategyService {

    /**
     * 插入数据

     */
    @PostMapping("/insertGrowthValueStrategy")
    DubboResult insertGrowthValueStrategy(@RequestBody EsGrowthStrategyDTO esGrowthStrategyDTO);


    /**
     * 根据查询收藏商品和店铺查询列表

     */
    @GetMapping("/getGrowthValueStrategy")
    DubboPageResult<EsGrowthValueStrategyDO> getGrowthValueStrategy();
}
