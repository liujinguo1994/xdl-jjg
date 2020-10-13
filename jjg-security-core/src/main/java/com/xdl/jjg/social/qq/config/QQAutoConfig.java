package com.xdl.jjg.social.qq.config;

import com.xdl.jjg.properties.SecurityProperties;
import com.xdl.jjg.social.qq.connect.QQConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.social.SocialAutoConfigurerAdapter;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionFactory;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 23:03
 */
@Configuration
@ConditionalOnProperty(prefix = "cy.security.social.qq", name = "app-id")
public class QQAutoConfig extends SocialAutoConfigurerAdapter {

    @Autowired
    private SecurityProperties securityProperties;

    @Override
    protected ConnectionFactory<?> createConnectionFactory() {
        System.out.println(securityProperties.getSocial().getQq().getProviderId());
        return new QQConnectionFactory(securityProperties.getSocial().getQq().getProviderId(), securityProperties.getSocial().getQq().getAppId(), securityProperties.getSocial().getQq().getAppSecret());
    }
}
