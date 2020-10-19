package com.xdl.jjg.roketmq;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/7/15 13:55
 */
@Setter
public class MQProducer implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQProducer.class);

    /**
     * namesrv地址
     */
    private String namesrvAddr;

    /**
     * 生产者对象
     */
    private DefaultMQProducer producer;

    public MQProducer(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isBlank(namesrvAddr)){
            throw new IllegalArgumentException("namesrvAddr不能为null");
        }
        producer = new DefaultMQProducer("defaultMQProducer");
        producer.setInstanceName("mqInstance_" + 1);
        producer.setNamesrvAddr(namesrvAddr);
        producer.setVipChannelEnabled(false);
        producer.start();
        LOGGER.info("MQProducer RocketMQ生成者启动成功");
    }

    /**
     * 发送消息
     * @param topic
     * @param message
     * @return
     * @throws Exception
     */
    public SendResult send(String topic, String message) throws Exception{
        LOGGER.info("RocketMQ发送消息,主题{},消息内容{}", topic, message);
        Message msg = new Message(topic, message.getBytes());
        producer.setMaxMessageSize(10485760);
        return producer.send(msg);
    }

    /**
     * 发送消息（超时时间）
     * @param topic
     * @param message
     * @param timeout
     * @return
     * @throws Exception
     */
    public SendResult send(String topic, String message,long timeout) throws Exception{
        LOGGER.info("RocketMQ发送消息,主题{},消息内容{}", topic, message);
        Message msg = new Message(topic, message.getBytes());
        return producer.send(msg,timeout);
    }

    /**
     * 发送消息（回调）
     * @param topic
     * @param message
     * @param sendCallback
     * @throws Exception
     */
    public void send(String topic, String message, SendCallback sendCallback) throws Exception{
        LOGGER.info("RocketMQ发送消息,主题{},消息内容{}", topic, message);
        Message msg = new Message(topic, message.getBytes());
        producer.send(msg,sendCallback);
    }

    /**
     * 发送消息（消息路由）
     * @param topic
     * @param message
     * @param messageQueueSelector
     * @param args
     * @return
     * @throws Exception
     */
    public SendResult send(String topic, String message, MessageQueueSelector messageQueueSelector,Object args) throws Exception{
        LOGGER.info("RocketMQ发送消息,主题{},消息内容{}", topic, message);
        Message msg = new Message(topic, message.getBytes());
        return producer.send(msg,messageQueueSelector,args);
    }

    @Override
    public void destroy() throws Exception {
        if(this.producer != null){
            this.producer.shutdown();
        }
    }
}
