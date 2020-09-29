package com.xdl.jjg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author qiushi
 * @version 1.0
 * @date 2019/7/9 14:46
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(EurekaServiceApp.class, args);
    }

}
