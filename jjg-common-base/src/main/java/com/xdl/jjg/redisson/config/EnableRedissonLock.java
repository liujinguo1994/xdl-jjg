package com.xdl.jjg.redisson.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 开启Redisson注解支持
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(RedissonAutoConfig.class)
public @interface EnableRedissonLock {
}
