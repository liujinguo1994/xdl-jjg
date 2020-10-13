package com.xdl.jjg.support;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/23 10:00
 */
@Data
@Component
@PropertySource(value = {"classpath:config/rocketMq.properties", "file:${spring.profiles.path}/config/rocketMq.properties"}, ignoreResourceNotFound = true)
public class MqProperties {

    @Value("${jjg.topic.account.accountTopic}")
    private String accountTopic;
    @Value("${jjg.topic.weiXin.weiXinPayTopic}")
    private String weiXinPayTopic;
    @Value("${jjg.topic.auction.auctionTopic}")
    private String auctionTopic;


    @Value("${jjg.group.weiXin.notifyGroup}")
    private String notifyGroup;
    @Value("${jjg.group.weiXin.refundNotifyGroup}")
    private String refundNotifyGroup;
    @Value("${jjg.group.account.registeredGroup}")
    private String registeredGroup;
    @Value("${jjg.group.auction.auctionEndGroup}")
    private String auctionEndGroup;
    @Value("${jjg.group.auction.auctionBeginGroup}")
    private String auctionBeginGroup;


    @Value("${jjg.tag.weiXin.notifyTag}")
    private String weiXinNotifyTag;
    @Value("${jjg.tag.weiXin.refundNotifyTag}")
    private String weiXinReFoundNotifyTag;
    @Value("${jjg.tag.account.registeredTag}")
    private String accountRegisteredTag;
    @Value("${jjg.tag.auction.timingEndTag}")
    private String timingEndTag;
    @Value("${jjg.tag.auction.timingBeginTag}")
    private String timingBeginTag;


    @Value("${jjg.accessKey}")
    private String accessKey;
    @Value("${jjg.secretKey}")
    private String secretKey;
    @Value("${jjg.nameSrvAddr}")
    private String nameSrvAddr;


}
