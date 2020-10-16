package com.xdl.jjg.config;/**
 * Description: zhuox-shop-member
 * <p>
 * Created by Administrator on 2020/5/25 10:07
 */

import com.alibaba.fastjson.JSON;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.aliyun.openservices.shade.com.alibaba.rocketmq.common.message.MessageExt;

import com.jjg.member.model.dto.EsMyFootprintDTO;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.web.service.IEsMyFootprintService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName MQFootprintMessageListener
 * @Description: TODO
 * @Author LIU
 * @Date 2020/5/25 
 * @Version V1.0
 * 会员足迹信息插入
 **/
public class MQFootprintMessageListener implements MessageListenerConcurrently {
    private static Logger logger = LoggerFactory.getLogger(MQFootprintMessageListener.class);

    @Autowired
    private IEsMyFootprintService iEsMyFootprintService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(list)){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt message = list.get(0);
        if(message.getBody() == null || message.getBody().length == 0){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        String body = new String(message.getBody());
        EsMyFootprintDTO esMyFootprintDTO = JSON.parseObject(body, EsMyFootprintDTO.class);
        logger.info("会员足迹消息接收成功");
        iEsMyFootprintService.insertMyFootprint(esMyFootprintDTO);

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }
}
