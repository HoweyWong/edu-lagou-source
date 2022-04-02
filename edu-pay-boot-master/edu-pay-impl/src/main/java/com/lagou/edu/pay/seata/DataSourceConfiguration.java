package com.lagou.edu.pay.seata;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.alibaba.druid.pool.DruidDataSource;

import io.seata.rm.datasource.DataSourceProxy;
import io.seata.spring.annotation.GlobalTransactionScanner;

/**
 * @Description:(数据源代理)   
 * @author: ma wei long
 * @date:   2020年7月28日 上午12:24:01
 */
@Configuration
public class DataSourceConfiguration {
	
	@Value("${spring.cloud.alibaba.seata.tx-service-group}")
    private String group;
    @Value("${spring.application.name}")
    private String appName;
    
	@Bean
	public FescarXidFilter fescarXidFilter(){
	    return new FescarXidFilter();
	}
	
	@Bean
	public GlobalTransactionScanner globalTransactionScanner(){
	    GlobalTransactionScanner scanner = new GlobalTransactionScanner(appName,group);
	    return scanner;
	}
	
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        DruidDataSource druidDataSource = new DruidDataSource();
        return druidDataSource;
    }

    @Primary
    @Bean("dataSource")
    public DataSourceProxy dataSource(DataSource druidDataSource){
        return new DataSourceProxy(druidDataSource);
    }

//    @Bean
//    public SqlSessionFactory sqlSessionFactory(DataSourceProxy dataSourceProxy)throws Exception{
//        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
//        sqlSessionFactoryBean.setDataSource(dataSourceProxy);
//        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
//                .getResources("classpath*:/mapper/*.xml"));
//        sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
//        return sqlSessionFactoryBean.getObject();
//    }

}

