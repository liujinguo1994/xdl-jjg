package com.xdl.jjg.social.weixinapp.config;

import com.xdl.jjg.properties.SecurityProperties;
import com.xdl.jjg.properties.WeiXinAppProperties;
import com.xdl.jjg.social.EasyConnectView;
import com.xdl.jjg.social.weixinapp.connect.WeixinAppConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.web.servlet.View;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 10:45
 */
@Configuration
@ConditionalOnProperty(prefix = "cy.security.social.weixinApp",name = "app-id")
public class WeixinAppAutoConfig extends SocialAutoConfigurerAdapter {
    @Autowired
    private SecurityProperties securityProperties;


    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        WeiXinAppProperties weixinConfig = securityProperties.getSocial().getWeixinApp();
        return new WeixinAppConnectionFactory(weixinConfig.getProviderId(), weixinConfig.getAppId(), weixinConfig.getAppSecret());
    }

    @Bean({"connect/weixinappConnect","connect/weixinappConnected"})
    @ConditionalOnMissingBean(name = "weixinConnectedView")
    public View weixinConnectedView(){
        return new EasyConnectView();
    }
}
