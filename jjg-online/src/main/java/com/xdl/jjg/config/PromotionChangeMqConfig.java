package com.xdl.jjg.config;

import com.xdl.jjg.roketmq.MQConsumer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/7/15 16:57
 */
@Configuration
public class PromotionChangeMqConfig {

    @Value("${rocketmq.after.namesrv}")
    private String namesrv;

    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    @Value("${rocketmq.promotion.change.groupname}")
    private String promotion_change_group;

    /**
     * 活动下架/过期消费者
     */
    @Bean
    public MQConsumer mqPromotionChangeConsumer(){
         return new MQConsumer(mqPromotionChangeListener(),namesrv,promotion_change_topic,promotion_change_group);
    }
    /**
     * 消费者监听实现
     * @return
     */
    @Bean
    public MessageListenerConcurrently mqPromotionChangeListener() {
        MessageListenerConcurrently listenerConcurrently = new MQCartPromotionChangeListener();
        return listenerConcurrently;
    }

}
