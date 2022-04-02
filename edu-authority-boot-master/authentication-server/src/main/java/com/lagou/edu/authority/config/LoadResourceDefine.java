package com.lagou.edu.authority.config;

import com.lagou.edu.authority.service.IResourceService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by zhoutaoo on 2018/5/25.
 */
@Component
class LoadResourceDefine implements InitializingBean {

    @Autowired
    private IResourceService resourceService;

    @Override
    public void afterPropertiesSet() throws Exception {
        resourceService.loadResource();
    }
}
