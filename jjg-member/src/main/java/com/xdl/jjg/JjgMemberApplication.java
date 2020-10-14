package com.xdl.jjg;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;


@SpringBootApplication(scanBasePackages = {"com.xdl.jjg"})
@EnableDiscoveryClient
@MapperScan("com.xdl.jjg.mapper")
@EnableEurekaClient
@EnableFeignClients("com.xdl.jjg.web.service")
public class JjgMemberApplication {

	public static void main(String[] args) {
		SpringApplication.run(JjgMemberApplication.class, args);
	}

}
