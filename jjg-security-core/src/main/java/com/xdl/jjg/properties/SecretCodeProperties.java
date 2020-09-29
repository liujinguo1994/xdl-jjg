package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/8/16 1:39
 */
public class SecretCodeProperties {
    /**
     * 验证码位数
     */
    private int length = 6;

    /**
     * 过期时间
     * 单位：秒
     */
    private int expireIn = 600;

    /**
     * 需要图形验证码拦截的url，多个url用,隔开
     */
    private String url;

    public SecretCodeProperties() {
    }

    public SecretCodeProperties(int length, int expireIn, String url) {
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
