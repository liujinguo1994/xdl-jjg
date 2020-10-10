package com.xdl.jjg;

import com.xdl.jjg.config.MybatisPlusConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/9 15:11
 */
@SpringBootApplication(scanBasePackages = {"com.xdl.jjg"})

//@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.xdl.jjg.mapper")
@EnableEurekaClient
@EnableFeignClients("com.xdl.jjg.web.service")
public class JjgServiceBootstrap extends WebMvcConfigurerAdapter {


    public static void main(String[] args) {
        SpringApplication.run(JjgServiceBootstrap.class, args);
    }


}
