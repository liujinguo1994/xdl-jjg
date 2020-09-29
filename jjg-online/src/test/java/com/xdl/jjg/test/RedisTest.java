package com.xdl.jjg.test;

import com.xdl.jjg.config.FastJsonSerializer;
import com.xdl.jjg.web.service.RedisService;
import com.xdl.jjg.web.service.impl.RedisServiceImpl;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */

public class RedisTest {


    public static void main(String[] args) {
        RedisTemplate redisTemplate = new RedisTest().redisTemplate();
        RedisService redisService = new RedisServiceImpl(redisTemplate);
        redisService.delKey("sysRewardParameterList");
//        System.out.println("*****set****" + set + "*********");


    }


    private RedisTemplate<String, Object> redisTemplate() {
        JedisConnectionFactory factory = new JedisConnectionFactory();
        factory.setHostName("r-bp1dedb6de4be0f4pd.redis.rds.aliyuncs.com");
        factory.setPassword("XDLjjg2019!");
        factory.setPort(6379);
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
