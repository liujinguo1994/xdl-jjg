package com.xdl.jjg.roketmq;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author: HQL 236154186@qq.com
 * @since: 2019/7/15 14:00
 */
@Setter
public class MQConsumer implements InitializingBean, DisposableBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(MQConsumer.class);

    private MessageListenerConcurrently messageListener;
    private String namesrvAddr;
    private String topic;
    private String groupName;
    private DefaultMQPushConsumer consumer;
    private MessageModel messageModel;

    /**
     * 默认是集群消费
     * @param messageListener
     * @param namesrvAddr
     * @param topic
     * @param groupName
     */
    public MQConsumer(MessageListenerConcurrently messageListener, String namesrvAddr, String topic, String groupName) {
        this.messageListener = messageListener;
        this.namesrvAddr = namesrvAddr;
        this.topic = topic;
        this.groupName = groupName;
    }

    /**
     * 可以设置消费类型（集群消费，广播）
     * @param messageListener
     * @param namesrvAddr
     * @param topic
     * @param groupName
     * @param consumer
     * @param messageModel
     */
    public MQConsumer(MessageListenerConcurrently messageListener, String namesrvAddr, String topic, String groupName, DefaultMQPushConsumer consumer, MessageModel messageModel) {
        this.messageListener = messageListener;
        this.namesrvAddr = namesrvAddr;
        this.topic = topic;
        this.groupName = groupName;
        this.consumer = consumer;
        this.messageModel = messageModel;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isBlank(namesrvAddr)){
            throw new IllegalArgumentException("namesrvAddr地址不能为空");
        }
        if(StringUtils.isBlank(topic)){
            throw new IllegalArgumentException("topic不能为空");
        }
        if(messageListener == null){
            throw new IllegalArgumentException("messageListener不能为null");
        }
        try {
            consumer = new DefaultMQPushConsumer(groupName);
            consumer.setInstanceName("mqConsumerInstance_" + 1);
            consumer.setNamesrvAddr(namesrvAddr);
            consumer.subscribe(topic, "*");
            if(null != messageModel){
                consumer.setMessageModel(messageModel);//消费模式
            }else{
                consumer.setMessageModel(MessageModel.CLUSTERING);//消费模式
            }
            consumer.registerMessageListener(messageListener);
            consumer.setVipChannelEnabled(false);
            consumer.start();
            LOGGER.info("MQConsumer RocketMQ消费者启动成功");
        } catch (Exception e) {
            LOGGER.info("MQConsumer RocketMQ消费者启动异常",e);
        }
    }

    @Override
    public void destroy() throws Exception {
        if(this.consumer != null){
            this.consumer.shutdown();
        }
    }
}
