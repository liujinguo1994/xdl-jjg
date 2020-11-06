package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsSiteNavigationDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface SiteNavigationService {



    //根据客户端类型查询导航菜单(PC,MOBILE)
    @GetMapping("/getByClientType")
    DubboPageResult<EsSiteNavigationDO> getByClientType(@RequestParam("clientType") String clientType);
}
