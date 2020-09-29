package com.xdl.jjg.test;

import com.xdl.jjg.OnlineApplication;
import com.xdl.jjg.mq.MQProducerConfig;
import com.xdl.jjg.model.mq.AccountRegisteredBeanMQ;
import com.xdl.jjg.util.Object2Array;
import com.xdl.jjg.web.service.MqService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/24 14:51
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {OnlineApplication.class})
public class MqServiceTest {
    @Value("${accountTopic}")
    public String topic;
    @Value("${accountRegisteredTag}")
    public String tag;

    @Autowired
    private MqService mqService;
//    @Autowired
//    private MqConsumer mqConsumer;

    @Test
    public void test1(){
        byte[] content = Object2Array.objectToByteArray("你好啊");
//        mqService.sendProducerMq(RocketMqConfig.getTopic(), RocketMqConfig.getTag(),"ddddddd");
    }
//
//    @Test
//    public void test2(){
//        mqConsumer.consumer();
//    }

    @Test
    public void test2(){
        AccountRegisteredBeanMQ bean = new AccountRegisteredBeanMQ(123, "","123", LocalDateTime.now());
        mqService.sendProducerMq(MQProducerConfig.getInstance().getProducerAccount(),topic,tag,bean);
    }
}
