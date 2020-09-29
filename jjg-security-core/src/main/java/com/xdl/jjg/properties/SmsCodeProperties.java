package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/21 8:21
 * TODO 短信验证码参数
 */
public class SmsCodeProperties {

    /**
     * 验证码位数
     */
    private int length = 4;

    /**
     * 过期时间
     * 单位：秒
     */
    private int expireIn = 180;

    /**
     * 需要短信验证码拦截的url，多个url用,隔开
     */
    private String url;

    public SmsCodeProperties() {
    }

    public SmsCodeProperties(int length, int expireIn, String url) {
        this.length = length;
        this.expireIn = expireIn;
        this.url = url;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
