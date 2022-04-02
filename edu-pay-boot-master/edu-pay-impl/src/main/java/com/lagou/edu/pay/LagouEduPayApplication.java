package com.lagou.edu.pay;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.retry.annotation.EnableRetry;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(支付服务)   
 * @author: ma wei long
 * @date:   2020年6月17日 上午10:47:47
*/
@SpringBootApplication(exclude = {
	    DataSourceAutoConfiguration.class
})
@EnableDiscoveryClient
@MapperScan("com.lagou.edu.pay.mapper")
@ComponentScan("com.lagou.edu")
@EnableFeignClients("com.lagou.edu")
@EnableRetry
@Slf4j
@EnableAutoDataSourceProxy
public class LagouEduPayApplication implements DisposableBean {

	private static ConfigurableApplicationContext ctx;
	
	public static void main(String[] args) {
    	ctx = SpringApplication.run(LagouEduPayApplication.class, args);
    	for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }
        log.info("spring cloud LagouEduPayApplication started!");
    }
	
    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()) {
            ctx.close();
        }
    }
}
