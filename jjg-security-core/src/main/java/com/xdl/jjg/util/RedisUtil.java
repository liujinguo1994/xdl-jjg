package com.xdl.jjg.util;

import com.xdl.jjg.properties.SecurityProperties;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/19 11:00
 */
public class RedisUtil {


    public RedisTemplate<String, Object> redisTemplate() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("114.55.248.198");
        factory.setPassword("ymxsy2008");
        factory.setPort(6388);
        factory.setDatabase(0);
        factory.afterPropertiesSet();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(factory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new FastJsonSerializer());
        redisTemplate.setValueSerializer(new FastJsonSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
