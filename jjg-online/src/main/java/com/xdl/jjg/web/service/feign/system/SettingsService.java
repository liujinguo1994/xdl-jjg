package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsSettingsDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(value = "jjg-manage")
public interface SettingsService {

    //根据业务标识查系统配置信息
    @GetMapping("/getByCfgGroup")
    DubboResult<EsSettingsDO> getByCfgGroup(@RequestParam("cfgGroup") String cfgGroup);
}
