package com.lagou.edu.order.shardingjdbc.config;

import com.baomidou.mybatisplus.core.incrementer.IKeyGenerator;
import com.baomidou.mybatisplus.extension.incrementer.H2KeyGenerator;
//import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: ma wei long
 * @date:   2020年7月28日 上午12:33:07
 */
@Configuration
public class MybatisPlusConfig {

  /**
   * mybatis-plus SQL执行效率插件【生产环境可以关闭】
   */
//  @Bean
//  public PerformanceInterceptor performanceInterceptor() {
//    return new PerformanceInterceptor();
//  }

  /**
   * 注入主键生成器
   */
  @Bean
  public IKeyGenerator keyGenerator() {
    return new H2KeyGenerator();
  }

}