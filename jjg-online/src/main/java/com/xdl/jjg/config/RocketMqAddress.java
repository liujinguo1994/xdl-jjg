package com.xdl.jjg.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/**
 * @Description: 连接RocketMQ服务器实体
 * @Author       LiuJG 344009799@qq.com
 * @Date         2020/5/7 10:55
 *
 */
@Data
@Configuration
public class RocketMqAddress {

    @Value("${rocketmq.after.namesrv}")
    private String namesrv;

    @Value("${rocketmq.trade.groupname}")
    private String tradeGroup;

}
