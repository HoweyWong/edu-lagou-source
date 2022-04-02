package com.lagou.edu.front.config.sentinel;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.zookeeper.ZookeeperDataSource;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月10日 下午12:07:08
 */
@Slf4j
@Component
public class DataSourceZookeeperInit {

    @Value("${spring.application.name}")
    private String groupId;
    @Value("${zookeeper.address}")
    private String zookeeperAddress;

    @PostConstruct
    public void init() throws Exception {
//    	final String path = "/sentinel_rule_config/" + groupId + "/front";
    	final String path = "/sentinel_rule_config/" + groupId;
        log.info("sentinel 数据源初始化 DataSourceZookeeperInit zookeeperAddress=={}",zookeeperAddress);
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new ZookeeperDataSource<>(zookeeperAddress, path,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {}));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
    }
}
