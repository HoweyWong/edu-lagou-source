package com.lagou.edu.boss.rest;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import com.lagou.edu.user.api.param.UserQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api(tags = "用户接口")
@Slf4j
public class UserController {

    @Autowired
    private UserRemoteService userRemoteService;

    @ApiOperation(value = "分页查询用户信息")
    @GetMapping("getUserPages")
    @ResponseBody
    public Result getUserPages(@RequestBody UserQueryParam userQueryParam) {
        log.info("分页查询用户信息:{}", JSON.toJSONString(userQueryParam));
        Integer currentPage = userQueryParam.getCurrentPage();
        Integer pageSize = userQueryParam.getPageSize();
        if (null == currentPage || currentPage <= 0) {
            userQueryParam.setCurrentPage(1);
        }
        if (null == pageSize || pageSize <= 0) {
            userQueryParam.setPageSize(10);
        }
        try {
            Page<UserDTO> queryCourses = userRemoteService.getUserPages(userQueryParam);
            return Result.success(queryCourses);
        } catch (Exception e) {
            log.error("分页查询用户信息:", e);
            return Result.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "封禁用户")
    @GetMapping("forbidUser")
    @ResponseBody
    public Result forbidUser(@RequestParam Integer userId) {
        log.info("封禁用户:{}", userId);
        try {
            boolean result = userRemoteService.forbidUser(userId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("分页查询用户信息:", e);
            return Result.fail(e.getMessage());
        }
    }
}