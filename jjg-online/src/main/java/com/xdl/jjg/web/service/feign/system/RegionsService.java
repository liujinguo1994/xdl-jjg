package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsRegionsDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
@FeignClient(value = "jjg-manage")
public interface RegionsService {



    //获取某地区的子地区
    @GetMapping("/getChildrenById")
    DubboResult<List<EsRegionsDO>> getChildrenById(@RequestParam("id") Long id);
}
