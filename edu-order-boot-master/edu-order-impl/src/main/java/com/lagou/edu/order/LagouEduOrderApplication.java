package com.lagou.edu.order;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;

import io.seata.spring.annotation.datasource.EnableAutoDataSourceProxy;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(订单服务启动类)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午4:36:16
*/
@SpringBootApplication(exclude = {
        DruidDataSourceAutoConfigure.class,
        DataSourceAutoConfiguration.class,
        MybatisPlusAutoConfiguration.class
})
@EnableConfigurationProperties
@EnableAutoDataSourceProxy
@EnableDiscoveryClient
@ComponentScan("com.lagou.edu")
@EnableFeignClients("com.lagou.edu")
@MapperScan("com.lagou.edu.order.mapper")
@Slf4j
public class LagouEduOrderApplication implements DisposableBean {

	private static ConfigurableApplicationContext ctx;
	
	public static void main(String[] args) {
    	ctx = SpringApplication.run(LagouEduOrderApplication.class, args);
    	for (String str : ctx.getEnvironment().getActiveProfiles()) {
            log.info(str);
        }
        log.info("spring cloud LagouEduOrderApplication started!");
    }
	
    @Override
    public void destroy() throws Exception {
        if (null != ctx && ctx.isRunning()) {
            ctx.close();
        }
    }
}
