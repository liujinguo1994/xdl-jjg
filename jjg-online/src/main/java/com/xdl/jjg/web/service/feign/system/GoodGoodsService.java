package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsGoodGoodsDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface GoodGoodsService {



    /**
     * 分页查询发布中的品质好货
     */
    @GetMapping("/getList")
    DubboPageResult<EsGoodGoodsDO> getList(@RequestParam("pageSize") int pageSize, @RequestParam("pageNum") int pageNum);
}
