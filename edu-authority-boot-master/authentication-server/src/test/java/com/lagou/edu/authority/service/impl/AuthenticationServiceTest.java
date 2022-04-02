package com.lagou.edu.authority.service.impl;

import com.lagou.edu.auth.client.dto.PermissionDTO;
import com.lagou.edu.authority.service.IAuthenticationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhoutaoo on 2018/5/26.
 */
@SpringBootTest()
@RunWith(SpringRunner.class)
public class AuthenticationServiceTest {

    @Autowired
    private IAuthenticationService authenticationService;

    @Test
    public void testListUserPermission() {
        PermissionDTO permissionDTO = authenticationService.listUserPermission(100030006);
        System.out.println(permissionDTO);
    }
}