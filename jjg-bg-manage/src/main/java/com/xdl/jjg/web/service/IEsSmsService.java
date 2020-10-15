package com.xdl.jjg.web.service;


import com.jjg.member.model.dto.EsSmsSendDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 手机短信接口
 */
public interface IEsSmsService {
    /**
     * 发送手机短信
     *
     * @param sendDTO
     */
    DubboResult send(EsSmsSendDTO sendDTO);

    /**
     * 发送国寿短信
     *
     * @param sendDTO
     */
    DubboResult sendLfc(EsSmsSendDTO sendDTO);
}