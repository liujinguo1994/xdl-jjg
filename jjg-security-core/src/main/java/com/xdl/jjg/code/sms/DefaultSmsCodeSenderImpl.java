package com.xdl.jjg.code.sms;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 8:06
 */
public class DefaultSmsCodeSenderImpl implements SmsCodeSender {

    @Override
    public void send(String mobile, String code) {
        System.out.println("向手机" + mobile + "发送短信验证码" + code);
    }
}
