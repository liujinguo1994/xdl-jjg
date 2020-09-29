package com.xdl.jjg.social.qq.connect;

import com.xdl.jjg.social.qq.api.QQ;
import com.xdl.jjg.social.qq.api.QQImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 17:54
 */
public class QQServiceProvider extends AbstractOAuth2ServiceProvider<QQ> implements OAuth2ServiceProvider<QQ> {

    private String appId;

    /**
     * 获取Authorization Code时访问的URL地址
     */
    private static final String URL_AUTHORIZE = "https://graph.qq.com/oauth2.0/authorize";
    /**
     * 通过使用Authorization Code获取Access Token
     * 获取QQ用户令牌时访问的URL地址
     */
    private static final String URL_ACCESS_TOKEN = "https://graph.qq.com/oauth2.0/token";

    /**
     * Create a new {@link QQOAuth2Template}.
     * @param appId
     * @param appSecret
     */
    public QQServiceProvider(String appId, String appSecret) {
        super(new QQOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
        this.appId = appId;
    }

    @Override
    public QQ getApi(String accessToken) {

        return new QQImpl(accessToken,appId);
    }
}
