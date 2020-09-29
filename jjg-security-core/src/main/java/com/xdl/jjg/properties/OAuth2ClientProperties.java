package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/6/29 23:20
 */
public class OAuth2ClientProperties {

    private String clientId;

    private String clientSecret;

    /**
     * token有效期
     * 默认值为0 发出去的token没有过期时间
     */
    private int accessTokenValiditySeconds;


    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public int getAccessTokenValiditySeconds() {
        return accessTokenValiditySeconds;
    }

    public void setAccessTokenValiditySeconds(int accessTokenValiditySeconds) {
        this.accessTokenValiditySeconds = accessTokenValiditySeconds;
    }
}
