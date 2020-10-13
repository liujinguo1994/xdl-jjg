package com.xdl.jjg.redisson.annotation;

import java.lang.annotation.*;

/**
 * Redisson分布式锁注解
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface DistributedLock {

    /**分布式锁名称*/
    String value() default "distributed-lock-redisson";
    /**锁超时时间,默认十秒*/
    int expireSeconds() default 10;
    /**获取锁最长等待时间（超过这个时间依旧没有获取成功则不再等待） */
    int waitSeconds() default 0;
}


