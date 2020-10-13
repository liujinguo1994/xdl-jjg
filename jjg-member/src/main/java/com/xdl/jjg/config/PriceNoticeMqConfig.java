package com.xdl.jjg.config;


import com.xdl.jjg.roketmq.MQConsumer;
import com.xdl.jjg.roketmq.MQProducer;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: zhuox-shop-member
 * <p>
 * Created by Administrator on 2019/7/15 16:57
 */
@Configuration
public class PriceNoticeMqConfig {

    @Value("${rocketmq.namesrv}")
    private String namesrv;

    @Value("${rocketmq.goods.change.topic}")
    private String topic;

    @Value("${rocketmq.goods.groupname}")
    private String groupname;

    /**
     * 配置生产者
     * @return
     */
    @Bean
    public MQProducer mqProducer(){
        MQProducer producer = new MQProducer(namesrv);
        return producer;
    }


    /**
     * 配置消费者
     * @return
     */
    @Bean
    public MQConsumer mqConsumer(){
        MQConsumer mqConsumer = new MQConsumer(mqMessageListener(),namesrv,topic,groupname);
        return mqConsumer;
    }

    /**
     * 消费者监听实现
     * @return
     */
    @Bean
    public MessageListenerConcurrently mqMessageListener(){
        MessageListenerConcurrently listenerConcurrently = new CutPriceNoticeListener();
        return listenerConcurrently;
    }
}
