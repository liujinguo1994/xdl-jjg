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
public class MemberMqConfig {

    @Value("${rocketmq.namesrv}")
    private String namesrv;

    @Value("${rocketmq.member.active.topic}")
    private String topic;
    @Value("${rocketmq.member.footprint.topic}")
    private String footTopic;
    @Value("${rocketmq.member.active.groupname}")
    private String groupname;
    @Value("${rocketmq.member.foot.groupname}")
    private String footGroupname;




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
     * 配置消费者 更新会员活跃信息
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
        MessageListenerConcurrently listenerConcurrently = new MQActiveChangeListener();
        return listenerConcurrently;
    }


    /**
     * 配置消费者 会员足迹信息
     * @return
     */
    @Bean
    public MQConsumer mqMemberFootprintConsumer(){
        MQConsumer mqConsumer = new MQConsumer(mqFootprintMessageListener(),namesrv,footTopic,footGroupname);
        return mqConsumer;
    }

    /**
     * 消费者监听实现
     * @return
     */
    @Bean
    public MessageListenerConcurrently mqFootprintMessageListener(){
        MessageListenerConcurrently listenerConcurrently = (MessageListenerConcurrently) new MQFootprintMessageListener();
        return listenerConcurrently;
    }
}
