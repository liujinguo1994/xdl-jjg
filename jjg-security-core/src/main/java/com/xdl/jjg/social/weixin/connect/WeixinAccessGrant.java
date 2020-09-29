package com.xdl.jjg.social.weixin.connect;

import org.springframework.social.oauth2.AccessGrant;

/**
 * @Author: ciyuan
 * @Date: 2019/5/26 4:28
 *
 * 微信的access_token信息，与标准的OAuth2协议不同，微信在获取access_token时会同时返回openId，并没有单独的通过accessToken换取openId的服务
 * 所以这里继承了标准的AccessGrant，添加了openId字段，作为对微信access_token信息的封装
 */
public class WeixinAccessGrant extends AccessGrant {

    private static final long serialVersionUID = 5096633867596440894L;

    private String openId;

    public WeixinAccessGrant() {
        super("");
    }


    public WeixinAccessGrant(String accessToken, String scope, String refreshToken, Long expiresIn) {
        super(accessToken, scope, refreshToken, expiresIn);
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
