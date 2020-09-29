package com.xdl.jjg.web.service;

import com.aliyun.openservices.ons.api.Producer;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/3日
 * @Description mq发送消息中心
 */
public interface MqService {


    /**
     * 同步发送消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param content
     * @return
     */
    void sendProducerMq(Producer producer, String topic, String tag, Object content);


    /**
     * 同步发送消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param key
     * @param content
     * @return
     */
    void sendProducerMq(Producer producer, String topic, String tag, String key, Object content);

    /**
     * 同步发送消息延迟消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param content
     * @param times
     * @return
     */
    void sendProducerDelayMq(Producer producer, String topic, String tag, Object content, Long times);

    /**
     * 同步发送消息延迟消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param key
     * @param content
     * @param times
     * @return
     */
    void sendProducerDelayMq(Producer producer, String topic, String tag, String key, Object content, Long times);

    /**
     * 异步发送消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param content
     * @return
     */
    void sendProducerAsyncMq(Producer producer, String topic, String tag, Object content);

    /**
     * 异步发送消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param key
     * @param content
     * @return
     */
    void sendProducerAsyncMq(Producer producer, String topic, String tag, String key, Object content);

    /**
     * 异步发送延迟消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param content
     * @param times
     * @return
     */
    void sendProducerDelayAsyncMq(Producer producer, String topic, String tag, Object content, Long times);

    /**
     * 异步发送延迟消息
     *
     * @param producer
     * @param topic
     * @param tag
     * @param key
     * @param content
     * @param times
     * @return
     */
    void sendProducerDelayAsyncMq(Producer producer, String topic, String tag, String key, Object content, Long times);
}
