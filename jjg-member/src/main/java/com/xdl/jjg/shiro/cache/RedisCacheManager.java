package com.xdl.jjg.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * 实现shiro的CacheManager
 */
@Component
public class RedisCacheManager implements CacheManager {

    @Value("${zhuox.shiro.cache}")
    private String redisShiroCache;

    //30分钟过期
    @Value("${zhuox.shiro.expire}")
    private int expire;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public <K, V> Cache<K, V> getCache(String name) throws CacheException {
        return new ShiroCache<K, V>(redisShiroCache, name, redisTemplate, expire);
    }

}