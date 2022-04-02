package com.lagou.edu.order.shardingjdbc.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dangdang.ddframe.rdb.sharding.keygen.DefaultKeyGenerator;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;

import io.seata.spring.annotation.GlobalTransactionScanner;
//import com.lagou.edu.order.shardingjdbc.rule.TableShardingAlgorithm;

/**
 * @author: ma wei long
 * @date:   2020年7月12日 下午10:26:02
 */
@Configuration
public class GlobalTransactionScannerConfig {

	@Value("${spring.cloud.alibaba.seata.tx-service-group}")
    private String group;
    @Value("${spring.application.name}")
    private String appName;
    
	@Bean
	public GlobalTransactionScanner globalTransactionScanner(){
		GlobalTransactionScanner scanner = new GlobalTransactionScanner(appName,group);
	    return scanner;
	}
	
	
    @Bean
    public KeyGenerator keygenerator() {
        return new DefaultKeyGenerator();
    }
}
