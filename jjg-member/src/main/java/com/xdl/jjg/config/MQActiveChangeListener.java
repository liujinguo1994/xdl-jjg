package com.xdl.jjg.config;

import com.alibaba.fastjson.JSON;
import com.shopx.common.util.JsonUtil;
import com.shopx.member.api.model.domain.dto.EsMemberActiveInfoDTO;
import com.shopx.member.api.model.domain.enums.ActiveTypeEnum;
import com.shopx.member.api.service.IEsMemberActiveInfoService;
import org.apache.dubbo.common.utils.CollectionUtils;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * mq 更新会员活跃信息
 * Description: zhuox-shop-member
 * <p>
 * Created by Administrator on 2019/7/17 10:23
 */
public class MQActiveChangeListener implements MessageListenerConcurrently {


    @Autowired
    private IEsMemberActiveInfoService iEsMemberActiveInfoService;

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


        Map<String,Object> activeQuantityMap = JSON.parseObject(body);
        activeQuantityMap.forEach((key, value) -> {
            List<EsMemberActiveInfoDTO> quantityList = JsonUtil.jsonToList(value.toString(), EsMemberActiveInfoDTO.class);
            if(StringUtils.isEquals(key, ActiveTypeEnum.ADD_ACTIVE.value())){
                if(CollectionUtils.isNotEmpty(quantityList)){
                    this.iEsMemberActiveInfoService.insertMemberActiveInfo(quantityList);
                }
            }else if(StringUtils.isEquals(key, ActiveTypeEnum.DELET_ACTIVE.value())){
                    List<String> orders = JsonUtil.jsonToList(value.toString(), String.class);
                    if(CollectionUtils.isNotEmpty(orders)){
                        this.iEsMemberActiveInfoService.deleteMemberActiveInfo(orders);
                    }
            }
        });

        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

    }
}
