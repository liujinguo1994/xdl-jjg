package com.xdl.jjg.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/26 4:03
 * 微信第三方登录配置类
 */
public class WeiXinProperties extends SocialProperties {

    /**
     * 第三方ID，用来决定发起第三方登录的url，默认是weixin
     */
    private String providerId = "weixin";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
