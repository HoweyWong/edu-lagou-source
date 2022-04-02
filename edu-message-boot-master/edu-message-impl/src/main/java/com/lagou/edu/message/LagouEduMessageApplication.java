package com.lagou.edu.message;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 上午10:50:37
*/
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("com.lagou.edu")
@EnableFeignClients("com.lagou.edu")
@MapperScan("com.lagou.edu.message.mapper")
@Slf4j
public class LagouEduMessageApplication implements DisposableBean {

	private static ConfigurableApplicationContext ctx;
	
	public static void main(String[] args) {
    	ctx = SpringApplication.run(LagouEduMessageApplication.class, args);
    	for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }
    	log.info("spring cloud LagouEduMessageApplication started!");
    }
	
    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()) {
            ctx.close();
        }
    }
}
