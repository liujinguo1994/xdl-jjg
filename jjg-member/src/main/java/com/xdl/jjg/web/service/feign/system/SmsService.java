package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.dto.EsSmsSendDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "jjg-manage")
public interface SmsService {


    /**
     * 发送手机短信
     *
     * @param sendDTO
     */
    @PostMapping("/sendSms")
    DubboResult send(EsSmsSendDTO sendDTO);


}
