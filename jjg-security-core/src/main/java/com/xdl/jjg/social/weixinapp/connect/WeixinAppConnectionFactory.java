package com.xdl.jjg.social.weixinapp.connect;

import com.xdl.jjg.social.weixin.api.Weixin;
import com.xdl.jjg.social.weixin.connect.WeixinAccessGrant;
import com.xdl.jjg.social.weixin.connect.WeixinAdapter;
import com.xdl.jjg.social.weixin.connect.WeixinServiceProvider;
import com.xdl.jjg.social.weixinapp.api.WeixinApp;
import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.support.OAuth2Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.social.oauth2.OAuth2ServiceProvider;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 10:37
 */
public class WeixinAppConnectionFactory extends OAuth2ConnectionFactory<WeixinApp> {



    public WeixinAppConnectionFactory(String providerId, String appId,String appSecret) {
        super(providerId, new WeixinAppServiceProvider(appId, appSecret), new WeixinAppAdapter());

    }

    @Override
    protected String extractProviderUserId(AccessGrant accessGrant) {
        if(accessGrant instanceof WeixinAccessGrant) {
            return ((WeixinAccessGrant)accessGrant).getOpenId();
        }
        return null;
    }

    @Override
    public Connection<WeixinApp> createConnection(AccessGrant accessGrant) {
        return new OAuth2Connection<WeixinApp>(getProviderId(), extractProviderUserId(accessGrant), accessGrant.getAccessToken(),
                accessGrant.getRefreshToken(), accessGrant.getExpireTime(), getOAuth2ServiceProvider(), getApiAdapter(extractProviderUserId(accessGrant)));
    }

    @Override
    public Connection<WeixinApp> createConnection(ConnectionData data) {
        return new OAuth2Connection<WeixinApp>(data, getOAuth2ServiceProvider(), getApiAdapter(data.getProviderUserId()));

    }

    protected ApiAdapter<WeixinApp> getApiAdapter(String providerUserId) {
        return new WeixinAppAdapter(providerUserId);
    }

    private OAuth2ServiceProvider<WeixinApp> getOAuth2ServiceProvider() {
        return (OAuth2ServiceProvider<WeixinApp>) getServiceProvider();
    }
}
