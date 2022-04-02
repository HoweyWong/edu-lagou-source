package com.lagou.edu.authority.service.impl;

import com.google.common.collect.Sets;
import com.lagou.edu.authority.service.IRoleMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

/**
 * @author : chenrg
 * @Description test for RoleMenuService
 * @create 2020/7/7 20:07
 **/
@SpringBootTest()
@RunWith(SpringRunner.class)
public class RoleMenuServiceTest {

    @Autowired
    private IRoleMenuService roleMenuService;

    @Test
    public void testQueryByRoleIds() {
        Set<Integer> menuIds = roleMenuService.queryByRoleIds(Sets.newHashSet(101, 102));
        System.out.println(menuIds);
    }

}
