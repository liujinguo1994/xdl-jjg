package com.xdl.jjg.social.weixinapp.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdl.jjg.social.weixin.api.WeixinUserInfo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.TokenStrategy;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/12 9:37
 */
public class WeixinAppImpl extends AbstractOAuth2ApiBinding implements WeixinApp {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;
    /**
     * 获取用户信息的url
     */
    private static final String URL_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?openid=";

    /**
     * @param accessToken
     */
    public WeixinAppImpl(String accessToken) {
        super(accessToken, TokenStrategy.ACCESS_TOKEN_PARAMETER);
    }

    /**
     * 默认注册的StringHttpMessageConverter字符集为ISO-8859-1,而微信返回的是UTF-8,所以覆盖了原来的方法
     *
     * @return
     */
    @Override
    protected List<HttpMessageConverter<?>> getMessageConverters() {
        List<HttpMessageConverter<?>> messageConverters = super.getMessageConverters();
        messageConverters.remove(0);
        messageConverters.add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return messageConverters;
    }


    /**
     * 获取微信的用户id
     *
     * @param openId
     * @return
     */
    @Override
    public WeixinUserInfo getUserInfo(String openId) {
        String url = URL_GET_USER_INFO + openId + "&lang=zh_CN";
        String response = getRestTemplate().getForObject(url, String.class);
        if (StringUtils.contains(response, "errcode")) {
            return null;
        }
        WeixinUserInfo userInfo = null;
        try {
            userInfo = objectMapper.readValue(response, WeixinUserInfo.class);
            //将用户信息存储到REDIS
            //redisTemplate.opsForValue().set(userInfo.getOpenid(), userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
        return userInfo;
    }
}
