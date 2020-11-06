package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.domain.EsAgreementDO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(value = "jjg-manage")
public interface AgreementService {


    //获取注册协议
    @GetMapping("/getEsAgreement")
    DubboResult<EsAgreementDO> getEsAgreement();
}
