package com.xdl.jjg.social.weixinapp.connect;

import com.xdl.jjg.social.weixin.connect.WeixinOAuth2Template;
import com.xdl.jjg.social.weixinapp.api.WeixinApp;
import com.xdl.jjg.social.weixinapp.api.WeixinAppImpl;
import org.springframework.social.oauth2.AbstractOAuth2ServiceProvider;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 9:59
 */
public class WeixinAppServiceProvider extends AbstractOAuth2ServiceProvider<WeixinApp> {

    /**
     * 微信获取授权码的url
     */
    private static final String URL_AUTHORIZE = "https://open.weixin.qq.com/connect/oauth2/authorize";

    /**
     * 微信获取accessToken的url
     */
    private static final String URL_ACCESS_TOKEN = "https://api.weixin.qq.com/sns/oauth2/access_token";

    /**
     * @param appId
     * @param appSecret
     */
    public WeixinAppServiceProvider(String appId, String appSecret) {
        super(new WeixinOAuth2Template(appId, appSecret, URL_AUTHORIZE, URL_ACCESS_TOKEN));
    }

    @Override
    public WeixinApp getApi(String accessToken) {
        return new WeixinAppImpl(accessToken);
    }
}
