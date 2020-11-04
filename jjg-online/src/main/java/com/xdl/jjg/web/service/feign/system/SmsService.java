package com.xdl.jjg.web.service.feign.system;

import com.jjg.system.model.dto.EsSmsSendDTO;
import com.xdl.jjg.response.service.DubboResult;
import org.springframework.web.bind.annotation.RequestBody;

public interface SmsService {

    /**
     * 发送国寿短信
     *
     * @param sendDTO
     */
    DubboResult sendLfc(@RequestBody EsSmsSendDTO sendDTO);

    /**
     * 发送手机短信
     *
     * @param sendDTO
     */
    DubboResult send(@RequestBody EsSmsSendDTO sendDTO);
}
