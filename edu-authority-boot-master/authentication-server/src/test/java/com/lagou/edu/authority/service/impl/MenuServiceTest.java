package com.lagou.edu.authority.service.impl;

import com.google.common.collect.Sets;
import com.lagou.edu.authority.entity.po.Menu;
import com.lagou.edu.authority.service.IMenuService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author : chenrg
 * @create 2020/7/8 10:14
 **/
@SpringBootTest()
@RunWith(SpringRunner.class)
public class MenuServiceTest {

    @Autowired
    private IMenuService menuService;

    @Test
    public void testQueryByRoleIds() {
        List<Menu> menus = menuService.queryByRoleIds(Sets.newHashSet(101, 102));
        menus.stream().forEach(m -> System.out.println(m));
    }

}
