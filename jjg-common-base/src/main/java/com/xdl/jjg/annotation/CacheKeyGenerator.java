package com.xdl.jjg.annotation;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/9/18 14:47
 * key生成器
 */
public interface CacheKeyGenerator {

    /**
     * 获取AOP参数,生成指定缓存Key
     *
     * @param pjp PJP
     * @return 缓存KEY
     */
    String getLockKey(ProceedingJoinPoint pjp);
}
