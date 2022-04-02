package com.lagou.edu.common.util;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(系统环境工具类)   
 * @author: ma wei long
 * @date:   2020年7月1日 下午2:38:31
*/
@Slf4j
@Component
public class EnvironmentUtils {

	@Value("${spring.profiles.active:pro}")
    String envName;
	
    static boolean isProduction = false;
    
    @PostConstruct
    public void init() {
    	isProduction = "pro".equalsIgnoreCase(envName);
    	log.info("EnvironmentUtils -init- isProduction :{}",isProduction);
    }
    
    /**
     * @Description: (是否线上环境)   
     * @author: ma wei long
     * @date:   2020年7月1日 下午2:40:14   
     */
    public static boolean isProd() {
    	return isProduction;
    }
}
