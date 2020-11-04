package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsLogiCompanyDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface LogiCompanyService {

    /**
     * 根据名字查询
     */
    DubboResult<EsLogiCompanyDO> getByName(@RequestParam("name") String name);

}
