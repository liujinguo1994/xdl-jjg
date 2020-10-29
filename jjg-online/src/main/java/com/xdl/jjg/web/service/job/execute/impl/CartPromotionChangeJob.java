package com.xdl.jjg.web.service.job.execute.impl;


import com.alibaba.fastjson.JSON;
import com.xdl.jjg.message.CartPromotionChangeMsg;
import com.xdl.jjg.roketmq.MQProducer;
import com.xdl.jjg.util.JsonUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Description: 购物车活动过期活动触发类
 * <p>
 * Created by LJG on 2019/8/29 9:17
 */
@JobHandler(value="cartPromotionChangeJobHandler")
@Component
public class CartPromotionChangeJob extends IJobHandler {
    protected final Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private MQProducer mqProducer;
    @Value("${rocketmq.promotion.change.topic}")
    private String promotion_change_topic;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        XxlJobLogger.log("XXL-JOB, 开始执行");

        try {
            CartPromotionChangeMsg cartPromotionChangeMsg = JsonUtil.jsonToObject(param, CartPromotionChangeMsg.class);
            mqProducer.send(promotion_change_topic, JSON.toJSONString(cartPromotionChangeMsg));
            logger.info("定时任务执行成功 已发送MQ");
        } catch (Exception e) {
            logger.error("活动变更发送Mq消息失败", e);
        }

        return SUCCESS;
    }
}
