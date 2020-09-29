package com.xdl.jjg.web.service.impl;

import com.aliyun.openservices.ons.api.*;
import com.xdl.jjg.util.Object2Array;
import com.xdl.jjg.web.service.MqService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年08月2019/8/3日
 * @Description mq发送消息中心
 */

@Service
public class MqServiceImpl implements MqService {


    private static Logger log = Logger.getLogger(MqServiceImpl.class);


    @Override
    public void sendProducerMq(Producer producer, String topic, String tag, Object content) {
        this.sendProducerMq(producer, topic, tag, null, content);
    }

    @Override
    public void sendProducerMq(Producer producer, String topic, String tag, String key, Object content) {
        this.sendProducerDelayMq(producer, topic, tag, key, content, null);
    }


    @Override
    public void sendProducerDelayMq(Producer producer, String topic, String tag, Object content, Long times) {
        this.sendProducerDelayMq(producer, topic, tag, null, content, times);
    }

    @Override
    public void sendProducerDelayMq(Producer producer, String topic, String tag, String key, Object content, Long times) {
        key = StringUtils.isNotBlank(key) ? key : "jjg";
        Message message = new Message(topic, tag, key, Object2Array.objectToByteArray(content));
        // 设置消息需要被投递的时间
        if (times != null && times > 0) {
            message.setStartDeliverTime(times);
        }

        try {
            SendResult sendResult = producer.send(message);
            // 同步发送消息，只要不抛异常就是成功
            assert sendResult != null;
            log.info(LocalDateTime.now() + ":****,Send mq message success, Topic is=" + topic + " ,tag is = " + tag + " ,key is = " + key + ",msgId is= " + sendResult.getMessageId());
        } catch (Exception e) {
            // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
            log.error(LocalDateTime.now() + ": ***,send message failed, Topic is=" + topic + " ,tag is = " + tag + " ,key is = " + key + ",msgId is= " + e.getMessage());
            log.error(e.getMessage());
        }

    }


    @Override
    public void sendProducerAsyncMq(Producer producer, String topic, String tag, Object content) {
        this.sendProducerAsyncMq(producer, topic, tag, null, content);
    }

    @Override
    public void sendProducerAsyncMq(Producer producer, String topic, String tag, String key, Object content) {
        this.sendProducerDelayAsyncMq(producer, topic, tag, key, content, null);
    }


    @Override
    public void sendProducerDelayAsyncMq(Producer producer, String topic, String tag, Object content, Long times) {
        this.sendProducerDelayAsyncMq(producer, topic, tag, null, content, times);
    }


    @Override
    public void sendProducerDelayAsyncMq(Producer producer, String topic, String tag, String key, Object content, Long times) {
        key = StringUtils.isNotBlank(key) ? key : "jjg";
        Message message = new Message(topic, tag, key, Object2Array.objectToByteArray(content));
        // 设置消息需要被投递的时间
        if (times != null && times > 0) {
            message.setStartDeliverTime(times);
        }
        producer.sendAsync(message, new SendCallback() {
            @Override
            public void onSuccess(final SendResult sendResult) {
                // 消费发送成功
                log.info(LocalDateTime.now() + ":****,Send mq message success, Topic is=" + topic + " ,tag is = " + tag + ",msgId is= " + sendResult.getMessageId());
            }

            @Override
            public void onException(OnExceptionContext context) {
                // 消息发送失败，需要进行重试处理，可重新发送这条消息或持久化这条数据进行补偿处理
                log.error(LocalDateTime.now() + ": ***,send message failed, Topic is=" + topic + " ,tag is = " + tag + ",msgId is= " + context.getMessageId());

            }
        });
    }


}
