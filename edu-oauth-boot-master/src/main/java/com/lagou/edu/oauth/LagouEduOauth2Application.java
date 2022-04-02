package com.lagou.edu.oauth;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCreateCacheAnnotation
@EnableFeignClients("com.lagou.edu")
public class LagouEduOauth2Application {

    public static void main(String[] args) {
        SpringApplication.run(LagouEduOauth2Application.class, args);
    }
}
