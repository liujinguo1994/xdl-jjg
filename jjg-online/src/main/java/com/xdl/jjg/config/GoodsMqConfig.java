package com.xdl.jjg.config;


import com.xdl.jjg.roketmq.MQProducer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** 商品减库存 生产者MQ
 * Description: zhuox-shop-trade
 * <p>
 * Created by Administrator on 2019/7/15 16:57
 */
@Configuration
public class GoodsMqConfig {

    @Value("${rocketmq.after.namesrv}")
    private String namesrv;

    /**
     * 配置生产者
     * @return
     */
    @Bean
    public MQProducer mqProducer(){
        MQProducer producer = new MQProducer(namesrv);
        return producer;
    }
}
