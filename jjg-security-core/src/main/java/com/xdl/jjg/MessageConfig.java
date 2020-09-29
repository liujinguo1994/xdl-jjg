package com.xdl.jjg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/8/29 11:13
 */
@Configuration
public class MessageConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource source = new ReloadableResourceBundleMessageSource();
        source.setBasename("classpath:org/springframework/security/messages");
        return source;
    }
}
