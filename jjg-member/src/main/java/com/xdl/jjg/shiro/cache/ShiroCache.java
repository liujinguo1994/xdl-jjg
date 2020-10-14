package com.xdl.jjg.shiro.cache;


import com.xdl.jjg.shiro.oath.ShiroUser;
import com.xdl.jjg.util.SerializeUtil;
import org.apache.ibatis.cache.CacheException;
import org.apache.shiro.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * ShiroCache（用户信息，权限信息）
 *
 * @param <K>
 * @param <V>
 */
public class ShiroCache<K, V> implements Cache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger(ShiroCache.class);

    private String cacheKey;
    private RedisTemplate redisTemplate;
    private int expire;

    public ShiroCache(String redisShiroCache, String name, RedisTemplate redisTemplate, int expire) {
        this.cacheKey = redisShiroCache + name + ":";
        this.redisTemplate = redisTemplate;
        this.expire = expire;
    }

    @Override
    public V get(K key) throws CacheException {
        try {
            if (null == key) {
                return null;
            } else {
                //序列化KEY
                byte[] k = serializeK(getCacheKey(key));
                //序列化VALUE
                byte[] v = (byte[]) redisTemplate.opsForValue().get(k);
                if (null != v) {
                    logger.debug("Getting object from cache [" + this.cacheKey + "] for key [" + key + "]key type:" + key.getClass());
                    return (V) deserializeV(v);
                }
                logger.debug("Element for [" + key + "] is null.");
                return null;
            }
        } catch (Exception t) {
            logger.error("Getting object from cache error ! ex:" + t);
            throw new CacheException(t);
        }
    }

    @Override
    public V put(K key, V value) throws CacheException {
        try {
            byte[] k = serializeK(getCacheKey(key));
            byte[] v = serializeV(value);
            redisTemplate.opsForValue().set(k, v, expire);
            logger.debug("Putting object in cache [" + this.cacheKey + "] for key [" + key + "]key type:"
                    + key.getClass() + "for value [" + value + "]value type:" + value.getClass());
            return value;
        } catch (Exception t) {
            logger.error("Putting object from cache error ! ex:" + t);
            throw new CacheException(t);
        }
    }

    @Override
    public V remove(K key) throws CacheException {
        V old = get(key);
        if (null != old) {
            byte[] k = serializeK(getCacheKey(key));
            redisTemplate.delete(k);
            logger.debug("Removing object from cache [" + this.cacheKey + "] for key [" + key + "]key type:" + key.getClass());
        }
        return old;
    }

    @Override
    public void clear() throws CacheException {
        //do nothing
    }

    @Override
    public int size() {
        return keys().size();
    }

    @Override
    public Set<K> keys() {
        byte[] k = serializeK(getCacheKey("*"));
        return (Set<K>) redisTemplate.opsForValue().get(k);
    }

    @Override
    public Collection<V> values() {
        Set<K> set = keys();
        List<V> list = new ArrayList<>();
        for (K s : set) {
            list.add(get(s));
        }
        return list;
    }

    private String getCacheKey(Object k) {
        if (k instanceof String) {
            return this.cacheKey + k;
        }
        if (k instanceof ShiroUser) {
            return this.cacheKey + ((ShiroUser) k).getId();
        }
        return this.cacheKey + k;
    }

    public String deserializeK(byte[] bytes) {
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    public byte[] serializeK(String string) {
        return string == null ? null : string.getBytes(StandardCharsets.UTF_8);
    }

    public V deserializeV(byte[] bytes) {
        return (V) SerializeUtil.unserialize(bytes);
    }

    public byte[] serializeV(Object object) {
        return SerializeUtil.serialize(object);
    }
}
