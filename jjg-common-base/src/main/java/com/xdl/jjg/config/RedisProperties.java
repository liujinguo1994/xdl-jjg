package com.xdl.jjg.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月2019/7/27日
 * @Description
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {

    private Integer database;
    private String host;
    private Integer port;
    private Integer timeout;
    private Integer maxClients;
    private String password;
    private Integer maxIdle;
    private Integer minIdle;
    private Integer maxWait;


}
