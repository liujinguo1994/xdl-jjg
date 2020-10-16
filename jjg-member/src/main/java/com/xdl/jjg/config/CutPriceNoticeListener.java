package com.xdl.jjg.config;


import com.jjg.shop.model.domain.EsGoodsMqDO;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import com.xdl.jjg.web.service.IEsMemberCollectionGoodsService;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * mq 更新会员活跃信息
 * Description: zhuox-shop-member
 * <p>
 * Created by Administrator on 2019/7/17 10:23
 */
public class CutPriceNoticeListener implements MessageListenerConcurrently {

    @Autowired
    private IEsMemberCollectionGoodsService iEsMemberCollectionGoodsService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
        if(CollectionUtils.isEmpty(list)){
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        if (CollectionUtils.isEmpty(list)) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        MessageExt message = list.get(0);
        if (message.getBody() == null || message.getBody().length == 0) {
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }
        String body = new String(message.getBody());
        List<EsGoodsMqDO> goodsMqDOList = JsonUtil.jsonToList(body, EsGoodsMqDO.class);
       /* if(CollectionUtils.isNotEmpty(goodsMqDOList)){
            goodsMqDOList.forEach(esGoodsMqDO -> {
                iEsMemberCollectionGoodsService.sendCutPriceSms(esGoodsMqDO.getId(),esGoodsMqDO.getShopId(),esGoodsMqDO.getMoney());
            });
        }*/
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }
}
