package com.xdl.jjg.code.sms;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 8:05
 */
public interface SmsCodeSender {

    /**
     * 发送短信验证码
     * @param mobile
     * @param code
     */
    void send(String mobile, String code);
}
