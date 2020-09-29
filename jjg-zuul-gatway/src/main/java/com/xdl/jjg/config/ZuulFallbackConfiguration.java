package com.xdl.jjg.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ZuulFallbackConfiguration{

	@Bean
	public BjManageFallback bjManageFallback(){
		
		return new BjManageFallback();
	}
	
	@Bean
	public OnlineFallback onlineFallback(){
		
		return new OnlineFallback();
	}

}
