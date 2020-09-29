package com.xdl.jjg.properties;

/**
 * @Author: ciyuan
 * @Date: 2019/6/29 23:20
 */
public class OAuth2Properties {

    /**
     * jwt生成Token所使用的密钥
     */
    private String jwtSignKey;

    private OAuth2ClientProperties[] clients = {};


    public String getJwtSignKey() {
        return jwtSignKey;
    }

    public void setJwtSignKey(String jwtSignKey) {
        this.jwtSignKey = jwtSignKey;
    }


    public OAuth2ClientProperties[] getClients() {
        return clients;
    }

    public void setClients(OAuth2ClientProperties[] clients) {
        this.clients = clients;
    }
}
