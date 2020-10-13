package com.xdl.jjg.redisson.annotation;

import com.xdl.jjg.redisson.RedissonLock;
import com.xdl.jjg.response.exception.RedissonLockException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Redisson分布式锁注解解析器
 */
@Aspect
public class DistributedLockHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DistributedLockHandler.class);

    private static final int LOCK_ERROR_CODE = 99;

    @Autowired
    RedissonLock redissonLock;

    @Around("@annotation(distributedLock)")
    public Object around(ProceedingJoinPoint joinPoint, DistributedLock distributedLock) throws RedissonLockException {
        /**获取锁名称*/
        String lockName = distributedLock.value();
        // 获取锁最长等待时长
        int waitSeconds = distributedLock.waitSeconds();
        /**获取超时时间，默认十秒*/
        int expireSeconds = distributedLock.expireSeconds();
        if (redissonLock.lock(lockName, expireSeconds,waitSeconds)) {
            try {
                return joinPoint.proceed();
            } catch (Throwable throwable) {
                LOGGER.error("获取Redis分布式锁[异常]，加锁失败", throwable);
                throw new RedissonLockException(LOCK_ERROR_CODE,"get redisson lock is error!",throwable);
            } finally {
                redissonLock.release(lockName);
                LOGGER.info("释放Redis分布式锁[成功]，解锁完成，结束业务逻辑...");
            }
        } else {
            LOGGER.error("获取Redis分布式锁[失败]");
            throw new RedissonLockException(LOCK_ERROR_CODE,"get redisson lock is error!");
        }
    }
}
