package com.lagou.edu.order.shardingjdbc.config;

import com.alibaba.druid.pool.DruidDataSource;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

/**
 * @author: ma wei long
 * @date:   2020年7月13日 上午11:16:08
 */
@Data
@ConfigurationProperties(prefix = "spring.shardingsphere.datasource.ds0")
@Component
public class Database0Config {
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private String databaseName = "database0";

    public DataSource createDataSource() {
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(getDriverClassName());
        result.setUrl(getUrl());
        result.setUsername(getUsername());
        result.setPassword(getPassword());
        return result;
    }
}
