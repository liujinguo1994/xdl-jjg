package com.xdl.jjg.web.service;


import com.jjg.system.model.dto.EsSendEmailDTO;
import com.xdl.jjg.response.service.DubboResult;

/**
 * 发送邮件接口
 */
public interface IEsSendEmailService {


    /**
     * 邮件发送实现，供消费者调用
     *
     * @param sendEmailDTO
     */
    DubboResult sendEmail(EsSendEmailDTO sendEmailDTO);

}
