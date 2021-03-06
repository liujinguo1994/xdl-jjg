package com.xdl.jjg;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月22日
 * @Description 手机客户端项目启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages = {"com.xdl.jjg.mapper"})
@EnableFeignClients("com.xdl.jjg.web.service")
@EnableAsync
public class OnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlineApplication.class, args);
    }

}
