package com.xdl.jjg.web.service;

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
public interface RedisService {


    /**
     * ----------------------------------- redis操作string类型-------------------------------------------------
     */

    /**
     * 新增
     *
     * @param key
     * @param object
     * @return
     */
    void saveObject(String key, Object object);

    /**
     * 新增
     *
     * @param key
     * @param object
     * @param liveTime 过期时间，单位秒
     * @return
     */
    void saveObject(final String key, final Object object, final Long liveTime);

    /**
     * 获取值
     *
     * @param key
     * @return
     */
    <T> T getObject(final String key);


    /**
     *  ---------------------------------redis操作list类型-----------------------------------------------------  */


    /**
     * 往list添加多个元素
     *
     * @param key
     * @param list
     * @return
     */
    Long saveList(final String key, final List list);


    /**
     * 获取list全部元素
     *
     * @param key
     * @return
     */
    <T> T getList(String key);


    /**
     * 获取list全部元素
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    <T> T getList(String key, long start, long end);


    /**
     *  -----------------------------------redis操作set类型---------------------------------------------------  */


    /**
     * 往set添加多个元素
     *
     * @param key
     * @param values
     * @return
     */
    Long saveSet(final String key, final Object... values);


    Boolean saveZSet(String key, String value, Long score);

    /**
     * 获取set元素
     *
     * @param key
     * @return
     */
    Object getSet(final String key);


    Set<String> getZSet(String key, Long start, Long end);

    Double getZSet(String key, String value);

    /**
     * 往set添加多个元素
     *
     * @param key
     * @param count
     * @return
     */
    <T> List<T> getSetMembers(final String key, Long count);


    /**
     * --------------------------------redis操作hash类型------------------------------------------------------
     */

    /**
     * 新增map
     *
     * @param key
     * @param field
     * @param value
     * @return
     */
    void saveHash(final String key, final String field, final Object value);

    /**
     * 新增map
     *
     * @param key
     * @param hashes
     * @return
     */
    void saveHash(final String key, final Map hashes);

    /**
     * 获取Hash全部元素
     *
     * @param key
     * @return
     */
    <T> Map<String, T> getHash(final String key);

    /**
     * 获取Hash元素
     *
     * @param key
     * @param field
     * @return
     */
    <T> T getHash(final String key, final String field);


    /**
     * --------------------------------redis操作自增长和失效时间---------------------------------------------------
     */

    /**
     * 自增
     *
     * @param key
     * @return
     */
    Long increment(final String key);

    /**
     * 自增
     *
     * @param key
     * @param delta
     * @return
     */
    Long increment(final String key, final Long delta);

    /**
     * 自增
     *
     * @param key
     * @param delta
     * @param liveTime
     * @return
     */
    Long increment(final String key, final Long delta, final Long liveTime);

    /**
     * 自增
     *
     * @param key
     * @param field
     * @return
     */
    Long hIncrement(String key, final String field);

    /**
     * 自增
     *
     * @param key
     * @param field
     * @param delta
     * @return
     */
    Long hIncrement(String key, final String field, final Long delta);

    /**
     * 设置有效期
     *
     * @param key
     * @param timeout
     * @param timeUnit
     * @return
     */
    Boolean expire(final String key, final Long timeout, TimeUnit timeUnit);

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    void delKey(final String key);

    /**
     * 删除排序值
     *
     * @param key
     * @param values
     * @return
     */
    void delZSet(String key, String values);

    /**
     * 删除key
     *
     * @param key
     * @param field
     * @return
     */
    Long delHash(final String key, final String field);
    /**
     * ------------------------------------------------------------------------------------- */

    /**
     * 加锁
     *
     * @param key
     * @param value 当前时间+超时时间
     * @return
     */
    boolean lock(String key, String value);

    /**
     * 解锁
     *
     * @param key
     */
    void unlock(String key);


}
