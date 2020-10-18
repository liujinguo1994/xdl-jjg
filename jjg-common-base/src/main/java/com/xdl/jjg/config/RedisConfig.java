package com.xdl.jjg.config;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author sanqi
 * @version 1.0
 * @data 2019年07月24日
 * @description redis配置文件
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    private final RedisProperties redisProperties;

    public RedisConfig(RedisProperties redisProperties) {
        this.redisProperties = redisProperties;
    }

    @Bean
    public JedisConnectionFactory oneConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisProperties.getHost());
        jedisConnectionFactory.setPassword(redisProperties.getPassword());
        jedisConnectionFactory.setPort(redisProperties.getPort());
        jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
        jedisConnectionFactory.setTimeout(redisProperties.getTimeout());
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //最大空闲数
        poolConfig.setMaxIdle(10);
        //最大连接数
        poolConfig.setMaxTotal(10);
        //最大等待毫秒
        poolConfig.setMaxWaitMillis(10000);
        jedisConnectionFactory.setPoolConfig(poolConfig);
        return jedisConnectionFactory;
    }


    @Bean
    public RedisTemplate redisTemplate() {
        RedisTemplate redisTemplate = new RedisTemplate<String, Object>();
        redisTemplate.setConnectionFactory(oneConnectionFactory());

        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashValueSerializer(new FastJsonSerializer());
        redisTemplate.setValueSerializer(new FastJsonSerializer());
        return redisTemplate;
    }


}