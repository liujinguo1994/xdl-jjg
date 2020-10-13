package com.xdl.jjg.lock;

import com.xdl.jjg.shiro.oath.ShiroKit;
import com.xdl.jjg.shiro.oath.ShiroUser;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisCluster;

@Component
@AllArgsConstructor
@Deprecated
public class CartPriceModifyLockUtil {
    // redis key前缀
    public static final String PRICE_LOCK_SESSION_ID_PREFIX = "PRICE_LOCK_SESSION_ID_PREFIX_";
    private static Logger logger = LoggerFactory.getLogger(CartPriceModifyLockUtil.class);
    private JedisCluster jedisCluster;

    /**
     * 设置用户购物车正在修改(加锁)
     *
     * @param userId 用户id
     */
    public void lock(Long userId) {
        Assert.notNull(userId, "用户id不可为空");
        // 加锁前需要等待其他线程释放锁
        waitUnlock(userId);
        // 加锁，最多锁5秒,如果已有锁，则重置5秒
        jedisCluster.setex(PRICE_LOCK_SESSION_ID_PREFIX + userId, 5, "modifying");
        logger.info("购物车价格修改锁——用户id:[{}]加锁完成", userId);
    }

    /**
     * 获取当前登录用户ID
     *
     * @return 当前登录用户ID
     */
    private Long getUserId() {
        ShiroUser user = ShiroKit.getUser();
        Assert.notNull(user, "用户登录状态异常");
        return user.getId();
    }

    public void lock() {
        lock(getUserId());
    }

    /**
     * 解锁
     *
     * @param userId 用户id
     */
    public void unlock(Long userId) {
        Assert.notNull(userId, "用户id不可为空");
        jedisCluster.del(PRICE_LOCK_SESSION_ID_PREFIX + userId);
        logger.info("购物车价格修改锁——用户id:[{}]解锁完成", userId);
    }

    /**
     * 解锁当前用户
     */
    public void unlock() {
        unlock(getUserId());
    }

    /**
     * 等待解锁
     *
     * @param userId 用户id
     */
    public void waitUnlock(Long userId) {
        Assert.notNull(userId, "用户id不可为空");
        long start = System.currentTimeMillis();
        logger.info("购物车价格修改锁——用户id:[{}]等待解锁开始----时间:[{}]", userId,start);
        while (jedisCluster.exists(PRICE_LOCK_SESSION_ID_PREFIX + userId)) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignore) {
            }
        }
        long end = System.currentTimeMillis();
        logger.info("购物车价格修改锁——用户id:[{}]等待解锁结束-----时间:[{}]----耗时:[{}]", userId,end,end-start);
    }

    /**
     * 等待当前用户解锁
     */
    public void waitUnlock() {
        waitUnlock(getUserId());
    }
}
