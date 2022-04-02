package com.lagou.edu.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.klock.KlockAutoConfiguration;
import org.springframework.boot.autoconfigure.klock.config.KlockConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 分布式锁配置
 * @author: ma wei long
 * @date:   2020年7月24日 下午4:43:07
*/
@Configuration
public class DistributedLockConfig {
	
	//需要用的分布式锁的服务 记得配置redis服务器
	@Value("${spring.redis.host:localhsot}")
    private String redisIp;
	@Value("${spring.redis.port:6379}")
    private String port;

	/**
	 * @author: ma wei long
	 * @date:   2020年7月24日 下午5:17:36   
	*/
	@Bean
    public KlockAutoConfiguration klockAutoConfiguration() {
        return new KlockAutoConfiguration();
    }
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月24日 下午5:17:36   
	*/
	@Bean
    public KlockConfig klockConfig() {
		KlockConfig klockConfig = new KlockConfig();
		klockConfig.setAddress(StringUtils.join(redisIp,":",port));
        return klockConfig;
    }
}
