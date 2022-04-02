package com.lagou.edu.authority.service;

import com.lagou.edu.auth.client.dto.PermissionDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public interface IAuthenticationService {

    /**
     * 验证用户是否有访问url的权限
     * @param userId
     * @param request
     * @return
     */
    boolean decide(String userId, HttpServletRequest request);

    /**
     * 查询用户菜单、资源权限
     * @param userId
     * @return
     */
    PermissionDTO listUserPermission(Integer userId);
}
