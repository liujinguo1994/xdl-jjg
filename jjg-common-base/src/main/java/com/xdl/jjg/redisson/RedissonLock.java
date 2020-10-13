package com.xdl.jjg.redisson;

import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁实现基于Redisson
 */
public class RedissonLock {

    private static final Logger LOGGER = LoggerFactory.getLogger(RedissonLock.class);

    RedissonManager redissonManager;

    public RedissonLock(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }

    public RedissonLock() {}
    /**
     * 加锁操作
     * @return
     */
    public boolean lock(String lockName, long expireSeconds) {
        return lock(lockName, expireSeconds,0);
    }

    /**
     * 加锁
     * @param lockName 锁名称
     * @param expireSeconds 锁超时时间
     * @param waitSeconds 获取锁最长等待时间
     * @return 是否成功
     */
    public boolean lock(String lockName, long expireSeconds,long waitSeconds){
        RLock rLock = redissonManager.getRedisson().getLock(lockName);
        boolean getLock = false;
        try {
            getLock = rLock.tryLock(waitSeconds, expireSeconds, TimeUnit.SECONDS);
            if (getLock) {
                LOGGER.info("获取Redisson分布式锁[成功],lockName={}", lockName);
            } else {
                LOGGER.info("获取Redisson分布式锁[失败],lockName={}", lockName);
            }
        } catch (InterruptedException e) {
            LOGGER.error("获取Redisson分布式锁[异常]，lockName=" + lockName, e);
            e.printStackTrace();
            return false;
        }
        return getLock;
    }

    /**
     * 解锁
     * @param lockName
     */
    public void release(String lockName) {
        redissonManager.getRedisson().getLock(lockName).unlock();
    }

    public RedissonManager getRedissonManager() {
        return redissonManager;
    }

    public void setRedissonManager(RedissonManager redissonManager) {
        this.redissonManager = redissonManager;
    }

}
