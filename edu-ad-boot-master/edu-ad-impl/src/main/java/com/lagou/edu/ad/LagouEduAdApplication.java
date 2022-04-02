package com.lagou.edu.ad;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.lagou.edu.ad.mapper")
@Slf4j
@EnableFeignClients("com.lagou.edu")
public class LagouEduAdApplication {

    public static void main(String[] args) {
        SpringApplication.run(LagouEduAdApplication.class, args);

        System.out.println("start....");
    }
}
