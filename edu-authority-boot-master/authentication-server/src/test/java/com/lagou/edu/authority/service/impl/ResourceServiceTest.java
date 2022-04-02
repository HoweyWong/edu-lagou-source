package com.lagou.edu.authority.service.impl;

import com.google.common.collect.Sets;
import com.lagou.edu.authority.entity.po.Resource;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResourceServiceTest {

    @Autowired
    private ResourceService resourceService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testQueryByRoleIds(){
        List<Resource> resources = resourceService.queryByRoleIds(Sets.newHashSet(101));
        resources.stream().forEach(r-> System.out.println(r));
    }
}