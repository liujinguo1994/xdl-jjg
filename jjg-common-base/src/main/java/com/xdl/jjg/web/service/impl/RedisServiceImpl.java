package com.xdl.jjg.web.service.impl;

import com.xdl.jjg.web.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author sanqi
 * @version 1.0
 * @data 2019年07月24日
 * @description redis服务类
 */
@Service
@SuppressWarnings("unchecked")
public class RedisServiceImpl implements RedisService {

    private Logger log = Logger.getLogger(this.getClass());
    @Autowired
    private RedisTemplate redisTemplate;


    public RedisServiceImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * ----------------------------------- redis操作string类型-------------------------------------------------
     */

    @Override
    public void saveObject(String key, Object object) {
        redisTemplate.opsForValue().set(key, object);
    }

    @Override
    public void saveObject(final String key, final Object object, final Long liveTime) {
        redisTemplate.opsForValue().set(key, object, liveTime, TimeUnit.SECONDS);
    }

    @Override
    public <T> T getObject(final String key) {
        return (T) redisTemplate.opsForValue().get(key);
    }


    /**
     *  ---------------------------------redis操作list类型-----------------------------------------------------  */

    /**
     * 往list添加多个元素
     */
    @Override
    public Long saveList(final String key, final List list) {
        return redisTemplate.opsForList().rightPushAll(key, list);
    }


    /**
     * 获取list全部元素
     */
    @Override
    public <T> T getList(String key) {
        return getList(key, 0, -1L);
    }

    /**
     * 获取list多个元素
     */
    @Override
    public <T> T getList(String key, long start, long end) {
        return (T) redisTemplate.opsForList().range(key, start, end);
    }


    /**
     *  -----------------------------------redis操作set类型---------------------------------------------------  */

    /**
     * 往set添加多个元素
     */
    @Override
    public Long saveSet(final String key, final Object... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 往set添加多个元素
     */
    @Override
    public Boolean saveZSet(final String key, String value, Long score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取set元素
     */
    @Override
    public Object getSet(final String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 获取set元素
     */
    @Override
    public Set<String> getZSet(final String key, Long start, Long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取set元素
     */
    @Override
    public Double getZSet(final String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 往set添加多个元素
     */
    @Override
    public <T> List<T> getSetMembers(final String key, Long count) {
        return redisTemplate.opsForSet().randomMembers(key, count);
    }


    /**
     * --------------------------------redis操作hash类型------------------------------------------------------
     */

    @Override
    public void saveHash(final String key, final String field, final Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    @Override
    public void saveHash(final String key, final Map hashes) {
        redisTemplate.opsForHash().putAll(key, hashes);
    }

    @Override
    public <T> Map<String, T> getHash(final String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    @Override
    public <T> T getHash(final String key, final String field) {
        return (T) redisTemplate.opsForHash().get(key, field);
    }


    /**
     * --------------------------------redis操作自增长和失效时间---------------------------------------------------
     */

    @Override
    public Long increment(final String key) {

        return increment(key, 1L);
    }

    @Override
    public Long increment(final String key, final Long delta) {
        return increment(key, delta, 0L);
    }

    @Override
    public Long increment(final String key, final Long delta, final Long liveTime) {
        Long res = redisTemplate.opsForValue().increment(key, delta);
        if (liveTime > 0) {
            redisTemplate.expire(key, liveTime, TimeUnit.SECONDS);
        }
        return res;
    }

    @Override
    public Long hIncrement(String key, final String field) {
        return hIncrement(key, field, 1L);
    }

    @Override
    public Long hIncrement(String key, final String field, final Long delta) {
        return redisTemplate.opsForHash().increment(key, field, delta);
    }

    @Override
    public Boolean expire(final String key, final Long timeout, TimeUnit timeUnit) {
        return redisTemplate.expire(key, timeout, timeUnit);
    }

    @Override
    public void delKey(final String key) {
        redisTemplate.delete(key);
    }


    @Override
    public void delZSet(String key, String values) {
        redisTemplate.opsForZSet().remove(key, values);
    }

    @Override
    public Long delHash(final String key, final String field) {
        return redisTemplate.opsForHash().delete(key, field);
    }
    /**
     * ------------------------------------------------------------------------------------- */

    /**
     * 加锁
     *
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    @Override
    public boolean lock(String key, String value) {
        //相当于SETNX指令，setIfAbsent方法设置了返回true,没有设置返回false
        if (redisTemplate.opsForValue().setIfAbsent(key, value)) {
            return true;
        }
        //假设currentValue=A   接下来并发进来的两个线程的value都是B  其中一个线程拿到锁,除非从始至终所有都是在并发（实际上这中情况是不存在的），
        // 只要开始时有数据有先后顺序，则分布式锁就不会出现“多卖”的现象
        Object currentValue = redisTemplate.opsForValue().get(key);
        //如果锁过期  解决死锁
        if (StringUtils.isNotBlank(currentValue + "") && Long.parseLong(currentValue.toString()) < System.currentTimeMillis()) {
            //获取上一个锁的时间，锁过期后，GETSET将原来的锁替换成新锁
            Object oldValue = redisTemplate.opsForValue().getAndSet(key, value);
            if (StringUtils.isNotBlank(oldValue + "")) {
                return true;
            }
        }
        //拿到锁的就有执行权力，拿不到的只有重新再来，重新再来只得是让用户手动继续抢单
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     */
    @Override
    public void unlock(String key) {
        try {
            Object currentValue = redisTemplate.opsForValue().get(key);
            if (StringUtils.isNotBlank(currentValue + "")) {
                redisTemplate.delete(key);
            }
        } catch (Exception e) {
            log.error("【redis分布式锁】解锁异常, {}", e);
        }
    }


}
