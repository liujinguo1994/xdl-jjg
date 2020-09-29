package com.xdl.jjg.social;

import com.xdl.jjg.exception.AppSecretException;
import com.xdl.jjg.social.weixin.api.Weixin;
import com.xdl.jjg.social.weixin.api.WeixinImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.concurrent.TimeUnit;

/**
 * @Author: ciyuan
 * @Date: 2019/6/29 11:14
 */
@Component
public class AppSignUpUtils {



    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private UsersConnectionRepository usersConnectionRepository;

    /**
     * 用于定位ConnectionFactory，可以根据ConnectionData创建Connection
     */
    @Autowired
    private ConnectionFactoryLocator connectionFactoryLocator;


    /**
     * 在redis中存储用户信息
     * key：通过请求中携带的设备id生成
     * value：即通过第三方获取的用户信息
     * timeout：10分钟，超时就清除缓存
     * @param request
     * @param connectionData
     */
    public void saveConnectionData(WebRequest request, ConnectionData connectionData) {
        redisTemplate.opsForValue().set(getKey(request), connectionData, 10, TimeUnit.MINUTES);
    }

    /**
     * 将第三方提供的用户信息与本地服务的用户的userId绑定
     * @param request
     * @param userId
     */
    public ConnectionData doPostSignUp(WebRequest request, String userId) {
        String key = (String) getKey(request);
        if (!redisTemplate.hasKey(key)) {
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        //将connectionData转换成c
        Connection<?> connection = connectionFactoryLocator.getConnectionFactory(connectionData.getProviderId())
                .createConnection(connectionData);
        usersConnectionRepository.createConnectionRepository(userId).addConnection(connection);
        //用户绑定信息之后就删除
        redisTemplate.delete(key);
        return connectionData;
    }

    public ConnectionData getConnectionData(WebRequest request,String userId){
        String key = (String) getKey(request);
        if (!redisTemplate.hasKey(key)) {
            throw new AppSecretException("无法找到缓存的用户社交账号信息");
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        return connectionData;
    }

    public ConnectionData getUser(WebRequest request,String userId){
        String key = (String) getKey(request);
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        ConnectionData connectionData = (ConnectionData) redisTemplate.opsForValue().get(key);
        return connectionData;
    }

    /**
     * 通过request请求携带的设备id生成redis存储的key值
     *
     * @param request
     * @return
     */
    public Object getKey(WebRequest request) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备id参数不能为空");
        }
        return "security:social.connect." + deviceId;
    }

    public Object getGrantKey(WebRequest request) {
        String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new AppSecretException("设备id参数不能为空");
        }
        return "security:social.connect.grant." + deviceId;
    }

}
