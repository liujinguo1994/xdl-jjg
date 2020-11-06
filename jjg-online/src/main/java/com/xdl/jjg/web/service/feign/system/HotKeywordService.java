package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsHotKeywordDO;
import com.xdl.jjg.response.service.DubboPageResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-manage")
public interface HotKeywordService {





    //查询热门关键字列表
    @GetMapping("/getList")
    DubboPageResult<EsHotKeywordDO> getList();
}
