package com.xdl.jjg.properties;

import org.springframework.boot.autoconfigure.social.SocialProperties;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 22:59
 */
public class QQProperties extends SocialProperties {

    /**
     * 社交平台接口提供商的标识
     */
    private String providerId = "qq";


    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }
}
