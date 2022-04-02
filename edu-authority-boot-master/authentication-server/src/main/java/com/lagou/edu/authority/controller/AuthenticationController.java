package com.lagou.edu.authority.controller;

import com.lagou.edu.auth.client.dto.PermissionDTO;
import com.lagou.edu.authority.service.IAuthenticationService;
import com.lagou.edu.common.entity.vo.Result;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api("权限验证")
@Slf4j
public class AuthenticationController {

    @Autowired
    IAuthenticationService authenticationService;

    @ApiOperation(value = "权限验证", notes = "根据用户userId，访问的url和method判断用户是否有权限访问")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户ID", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "url", value = "访问的url", required = true, dataType = "string"),
            @ApiImplicitParam(paramType = "query", name = "method", value = "访问的method", required = true, dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping(value = "/auth/permission")
    public Result decide(@RequestParam String userId, @RequestParam String url,
                         @RequestParam String method, HttpServletRequest request) {
        boolean decide = authenticationService.decide(userId, new HttpServletRequestAuthWrapper(request, url, method));
        log.info("Auth permission. userId:{}, url:{}, method:{}, permission:{}", userId, url, method, decide);
        return Result.success(decide);
    }

    @ApiOperation(value = "获取用户权限列表", notes = "根据用户userId查询用户菜单、资源权限")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "登录用户ID", required = true, dataType = "string")
    })
    @ApiResponses(@ApiResponse(code = 200, message = "处理成功", response = Result.class))
    @PostMapping("/auth/listUserPermission")
    public Result<PermissionDTO> listUserPermission(@RequestParam Integer userId) {
        log.info("List user permissions. userId:{}", userId);
        PermissionDTO permission = authenticationService.listUserPermission(userId);
        return Result.success(permission);
    }

}