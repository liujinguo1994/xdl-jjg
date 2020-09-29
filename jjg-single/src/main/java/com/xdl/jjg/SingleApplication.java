package com.xdl.jjg;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;


/**
 * @author sanqi
 * @version 1.0.0
 * @data 2019年07月27日
 * @Description 项目启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"com.xdl.jjg"})
@MapperScan(basePackages = {"com.xdl.jjg.mapper"})
@EnableFeignClients("com.xdl.jjg")
@EnableAsync
public class SingleApplication {

    public static void main(String[] args) {
        SpringApplication.run(SingleApplication.class, args);
    }


}
