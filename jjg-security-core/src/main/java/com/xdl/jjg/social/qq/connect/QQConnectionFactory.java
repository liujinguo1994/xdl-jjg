package com.xdl.jjg.social.qq.connect;

import com.xdl.jjg.social.qq.api.QQ;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;

/**
 * @Author: ciyuan
 * @Date: 2019/5/25 22:30
 */
public class QQConnectionFactory extends OAuth2ConnectionFactory<QQ> {


    /**
     * Create a {@link OAuth2ConnectionFactory}.
     *
     * @param providerId 提供商的唯一标识
     * @param appId
     * @param appSecret  serviceProvider ServiceProvider模型，用于执行授权流程并获取本机服务API实例。{@link QQServiceProvider}
     *                   apiAdapter      ApiAdapter用于将特定于提供程序的服务API模型映射到统一的{@link Connection}接口。
     */
    public QQConnectionFactory(String providerId, String appId, String appSecret) {
        super(providerId, new QQServiceProvider(appId, appSecret), new QQAdapter());
    }
}
