package com.lagou.edu.boss.rest;

import com.lagou.edu.auth.client.dto.PermissionDTO;
import com.lagou.edu.auth.client.provider.AuthProvider;
import com.lagou.edu.boss.common.UserManager;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.common.result.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户权限管理
 *
 * @author : chenrg
 * @create 2020/7/8 18:05
 **/
@Api(tags = "用户权限", produces = "application/json")
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {


    @Autowired
    private AuthProvider authProvider;

    @ApiOperation(value = "获取用户菜单和资源权限列表")
    @GetMapping("/getUserPermissions")
    public ResponseDTO<PermissionDTO> getUserPermissions() {
        Integer userId = UserManager.getUserId();
        try {
            Result<PermissionDTO> result = authProvider.listUserPermission(userId);
            if (result.isSuccess()) {
                return ResponseDTO.success(result.getData());
            }
        } catch (Exception e) {
            log.error("Query user permissions failed. userId:{}", userId, e);
            return ResponseDTO.ofError("查询用户权限失败！");
        }
        return ResponseDTO.ofError("查询用户权限失败！");
    }
}
