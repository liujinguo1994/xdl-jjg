package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsFocusPictureDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface FocusPictureService {


    //根据客户端类型查询轮播图列表
    @GetMapping("/getList")
    DubboPageResult<EsFocusPictureDO> getList(@RequestParam("clientType") String clientType);
}
