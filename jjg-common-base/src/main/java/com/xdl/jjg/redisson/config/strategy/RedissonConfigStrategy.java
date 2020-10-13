package com.xdl.jjg.redisson.config.strategy;

import com.xdl.jjg.redisson.config.RedissonProperties;
import org.redisson.config.Config;

/**
 * Redisson配置构建接口
 */
public interface RedissonConfigStrategy {

    /**
     * 根据不同的Redis配置策略创建对应的Config
     * @param redissonProperties
     * @return Config
     */
    Config createRedissonConfig(RedissonProperties redissonProperties);
}
