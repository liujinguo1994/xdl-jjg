package com.xdl.jjg.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/10 15:25
 */
public class WeiXinAppProperties extends SocialProperties {

    /**
     * 第三方ID，用来决定发起第三方登录的url，默认是weixinapp
     *
     */
    private String providerId = "weixinapp";

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

}
