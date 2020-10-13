package com.xdl.jjg.manager;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 域名配置
 *
 * @author kingapex
 * @version 1.0
 * @since 7.0.0
 * 2018/6/20
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "zhuox.domain")
public class DomainSettings {

    /**
     * 主域名
     */
    private String main;

    /**
     * 买家端域名
     */
    private String buyer;

    /**
     * 手机买家端域名
     */
    private String mobileBuyer;
}
