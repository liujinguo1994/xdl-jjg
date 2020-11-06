package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsPageDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface PageService {

    //使用客户端类型和页面类型查询一个楼层
    @GetMapping("/getByType")
    DubboResult<EsPageDO> getByType(@RequestParam("clientType") String clientType, @RequestParam("pageType") String pageType);

}
