package com.lagou.edu.authority.config;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author : chenrg
 * @create 2020/7/9 10:17
 **/
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 放过所有请求，网关已经做了权限验证
        http.authorizeRequests().anyRequest().permitAll();
    }
}
