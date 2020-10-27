package com.xdl.jjg.config;

import com.alibaba.fastjson.JSON;
import com.jjg.trade.model.enums.PromotionTypeEnum;
import com.jjg.trade.model.vo.CartItemsVO;
import com.jjg.trade.model.vo.CartVO;
import com.jjg.trade.model.vo.TradePromotionGoodsVO;
import com.xdl.jjg.manager.CartManager;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.util.CollectionUtils;
import com.xdl.jjg.util.JsonUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * mq 购物车活动数据刷新
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/7/17 10:23
 */
public class MQCartPromotionChangeListener implements MessageListenerConcurrently {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private JedisCluster jedisCluster;

    @Autowired
    private CartManager cartManager;
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

        CartPromotionChangeMsg cartPromotionChangeMsg = JsonUtil.jsonToObject(body, CartPromotionChangeMsg.class);

        Set<String> result = new HashSet<>();
        try {
            // 获取Redis集群内所有节点
            Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();

            for (Map.Entry<String, JedisPool> entry : clusterNodes.entrySet()) {
                JedisPool value = entry.getValue();
                Jedis jedis = value.getResource();
                // 判断非从节点(因为若主从复制，从节点会跟随主节点的变化而变化)
                if (!jedis.info("replication").contains("role:slave")) {
                    // 搜索单个节点内匹配的Key
                    Set<String> keys = jedis.keys("CART_MEMBER_ID_KEY_*");
                    // 合并搜索结果
                    result.addAll(keys);
                }
                jedis.close();
            }
        } catch (Exception e) {
            logger.error("获取key异常", e);
        }
        try {
            Thread.sleep(5000);
            result.forEach(key -> {

                // 通过key获取该会员的购物车数据
                String cartMsg = jedisCluster.get(key);
                List<CartVO> cartVOList = JSON.parseArray(cartMsg, CartVO.class);
                jedisCluster.del(key);
                cartVOList.forEach(cartVO -> {
                    List<CartItemsVO> cartItemsList = cartVO.getCartItemsList();
                    cartItemsList.forEach(cartItemsVO -> {
                        if (cartItemsVO.getPromotionList().size() > 1){
                            logger.info("活动集合大于1的skuID[{}]",cartItemsVO.getSkuId());
                            List<TradePromotionGoodsVO> promotionList = cartItemsVO.getPromotionList();
                            Long activityId = cartPromotionChangeMsg.getActivityId();
                            String promotionType = cartPromotionChangeMsg.getPromotionType();
                            // 如果是秒杀 则添加结束时间进行匹配筛选
                            if (PromotionTypeEnum.SECKILL.name().equals(promotionType)){
                                List<TradePromotionGoodsVO> collect = promotionList.stream()
                                        .filter(tradePromotionGoodsVO -> tradePromotionGoodsVO.getActivityId().equals(activityId)
                                                && tradePromotionGoodsVO.getPromotionType().equals(promotionType)
                                                && tradePromotionGoodsVO.getEndTime() <= System.currentTimeMillis()).collect(Collectors.toList());
                                if (collect.size() > 0){
                                    cartManager.getPromotionChangeList(cartItemsVO);
                                }
                            }else {
                                List<TradePromotionGoodsVO> collect = promotionList.stream()
                                        .filter(tradePromotionGoodsVO -> tradePromotionGoodsVO.getActivityId().equals(activityId)
                                                && tradePromotionGoodsVO.getPromotionType().equals(promotionType)).collect(Collectors.toList());
                                if (collect.size() > 0){
                                    cartManager.getPromotionChangeList(cartItemsVO);
                                }
                            }
                        }
                    });
                });
                logger.info("消息消费，购物车key[{}]",key);
                jedisCluster.set(key, JSON.toJSONString(cartVOList));
            });
        } catch (Exception e) {
            logger.error("更新活动数据异常", e);
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }


}
