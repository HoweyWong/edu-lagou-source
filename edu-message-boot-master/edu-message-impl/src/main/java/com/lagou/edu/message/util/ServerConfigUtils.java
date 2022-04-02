package com.lagou.edu.message.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月30日 下午5:07:31
 */
@Slf4j
@Data
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ServerConfigUtils {

	@Autowired
    private ApplicationContext context;
	
	public static ServerConfigUtils instance;
	
	@Value("${spring.security.oauth2.jwt.signingKey}")
    private String signingKey;
    @Value("${spring.application.name}")
    private String appName;
    @Value("${webSocket.origin}")
    private String webSocketOrigin;
    @Value("${webSocket.context}")
    private String webSocketContext;
    @Value("${webSocket.port}")
    private Integer webSocketPort;
    
    @Value("${spring.redis.host}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private Integer redisPort;
    
    
    
    @PostConstruct
    public void init() {
		try {
			instance = (ServerConfigUtils)context.getBean(Class.forName("com.lagou.edu.message.util.ServerConfigUtils"));
		} catch (Exception e) {
			log.error("ServerConfigUtils - init - error",e);
		}
    }
}
