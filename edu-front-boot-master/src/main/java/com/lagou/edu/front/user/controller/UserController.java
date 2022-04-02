package com.lagou.edu.front.user.controller;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.regex.RegexUtil;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.front.common.UserManager;
import com.lagou.edu.front.user.service.OAuthRemoteService;
import com.lagou.edu.front.user.service.UserService;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.UserWeixinRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import com.lagou.edu.user.api.dto.WeixinDTO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RefreshScope
@RestController
@RequestMapping("/user/")
@Api(tags = "用户接口", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class UserController {

    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UserWeixinRemoteService userWeixinRemoteService;
    @Autowired
    private OAuthRemoteService oAuthRemoteService;
    @Autowired
    private UserService userService;

    @Value("${spring.oauth.client_id}")
    private String clientId;
    @Value("${spring.oauth.client_secret}")
    private String clientSecret;
    @Value("${spring.oauth.scope}")
    private String scope;
    @Value("${spring.oauth.grant_type}")
    private String grantType;
    @Value("${spring.oauth.refresh_grant_type}")
    private String refreshGrantType;

    @ApiOperation(value = "用户登录", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "用户登录, 未注册用户会自动注册并登录")
    @ApiResponses({
            @ApiResponse(code = 201, message = "非法的手机号"),
            @ApiResponse(code = 202, message = "密码或者验证码为空"),
            @ApiResponse(code = 203, message = "验证码错误"),
            @ApiResponse(code = 204, message = "验证码过期"),
            @ApiResponse(code = 205, message = "手机号或者密码错误"),
            @ApiResponse(code = 206, message = "登录失败"),
            @ApiResponse(code = 207, message = "验证码错误"),
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 1, message = "success")
    })
    @PostMapping("login")
    public ResponseDTO<String> login(@ApiParam(name = "phone", value = "手机号", required = true) String phone,
                                     @ApiParam(name = "password", value = "密码") String password,
                                     @ApiParam(name = "code", value = "验证码") String code) {
        if (StringUtils.isEmpty(phone) || !RegexUtil.isPhone(phone)) {
            return ResponseDTO.ofError(201, "非法的手机号");
        }
        if (StringUtils.isEmpty(password) && StringUtils.isEmpty(code)) {
            return ResponseDTO.ofError(202, "密码或者验证码为空");
        }
        // 默认是账号密码登录
        Integer type = 0;
        if (StringUtils.isEmpty(password) && StringUtils.isNotEmpty(code)) {
            type = 1;
        }

        // 判断手机号是否有注册
        boolean isRegister = this.userRemoteService.isRegister(phone);
        if (!Boolean.TRUE.equals(isRegister)) {
            // 不允许直接手机号+密码注册并登录
            if (type == 0) {
                log.info("该手机号[{}]未注册,不允许手机号+密码注册", phone);
                return ResponseDTO.ofError(205, "手机号或者密码错误");
            }
            UserDTO dto = new UserDTO();
            dto.setAccountNonExpired(true);
            dto.setAccountNonLocked(true);
            dto.setCredentialsNonExpired(true);
            dto.setStatus("ENABLE");
            dto.setRegIp(UserManager.getUserIP());
            dto.setName(phone);
            dto.setPassword(type == 0 ? password : phone);
            dto.setCreateTime(new Date());
            dto.setIsDel(false);
            dto.setPhone(phone);
            dto.setUpdateTime(new Date());
            dto.setPortrait(null);
            UserDTO newUser = this.userRemoteService.saveUser(dto);
            log.info("用户[{}]注册成功", newUser);
        }
        return this.userService.createAuthToken(phone, password, code, type);
    }

    @ApiOperation(value = "用户退出", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "用户退出")
    @ApiResponses({
            @ApiResponse(code = 1, message = "success")
    })
    @PostMapping("logout")
    public ResponseDTO<String> logout() {
        // TODO 删除refresh_token,access_token
        Integer userId = UserManager.getUserId();
        log.info("============用户[{}]退出登录================", userId);
        return ResponseDTO.success();
    }

    @ApiOperation(value = "刷新token", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "根据refresh_token重新获取access_token")
    @PostMapping("refresh_token")
    @ApiResponses({
            @ApiResponse(code = 201, message = "refresh_token为空"),
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 1, message = "success")
    })
    public ResponseDTO<String> refreshToken(String refreshtoken) {
        if (StringUtils.isEmpty(refreshtoken)) {
            return ResponseDTO.ofError(201, "refresh_token为空");
        }
        String token = this.oAuthRemoteService.refreshToken(refreshtoken, refreshGrantType, clientId, clientSecret);
        if (StringUtils.isBlank(token)) {
            return ResponseDTO.response(-1, "login fail", null);
        }
        String userId = JSON.parseObject(token).getString("user_id");
        if (StringUtils.isBlank(userId)) {
            return ResponseDTO.response(-1, "login fail", null);
        }
        return ResponseDTO.response(1, "success", token);
    }

    @ApiOperation(value = "用户基本信息", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "获取用户基本信息")
    @GetMapping("getInfo")
    @ApiResponses({
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 1, message = "success")
    })
    public ResponseDTO<Map<String, Object>> getInfo() {

        Integer userId = UserManager.getUserId();
        if (null == userId || userId <= 0) {
            return ResponseDTO.response(-1, "login fail");
        }

        UserDTO userDTO = this.userRemoteService.getUserById(userId);
        if (null == userDTO) {
            return ResponseDTO.response(-1, "login fail");
        }
        Map<String, Object> result = new HashMap<>();
        result.put("userName", userDTO.getName());
        result.put("portrait", userDTO.getPortrait());
        try {
            result.put("isUpdatedPassword", this.userRemoteService.isUpdatedPassword(userId));
        } catch (Exception e) {
            log.error("获取用户[{}]是否修改过密码发生异常", userId, e);
        }
        try {
            // 微信昵称
            WeixinDTO weixin = this.userWeixinRemoteService.getUserWeixinByUserId(userId);
            if (null != weixin) {
                result.put("weixinNickName", weixin.getNickName());
            }
        } catch (Exception e) {
            log.error("获取用户[{}]微信昵称发生异常", userId, e);
        }
        return ResponseDTO.success(result);
    }

    @ApiOperation(value = "更新用户基本信息", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "更新用户基本信息")
    @PostMapping("updateInfo")
    @ApiResponses({
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 201, message = "非法的用户昵称"),
            @ApiResponse(code = 202, message = "非法的用户头像"),
            @ApiResponse(code = 203, message = "用户信息更新失败"),
            @ApiResponse(code = 1, message = "success")
    })
    public ResponseDTO<Map<String, Object>> updateInfo(@ApiParam(name = "userName", value = "用户昵称") String userName,
                                                       @ApiParam(name = "portrait", value = "用户头像URL") String portrait) {

        Integer userId = UserManager.getUserId();
        if (null == userId || userId <= 0) {
            return ResponseDTO.response(-1, "login fail");
        }
        if (StringUtils.isBlank(userName) || StringUtils.length(userName) > 20) {
            return ResponseDTO.response(201, "非法的用户昵称");
        }
        if (StringUtils.isBlank(portrait) || !StringUtils.startsWith(portrait, "http")) {
            return ResponseDTO.response(202, "非法的用户头像");
        }

        UserDTO dto = new UserDTO();
        dto.setId(userId);
        dto.setPortrait(portrait);
        dto.setName(userName);

        boolean result = this.userRemoteService.updateUser(dto);
        if (!Boolean.TRUE.equals(result)) {
            return ResponseDTO.response(203, "用户信息更新失败");
        }
        return ResponseDTO.success();
    }

    @ApiOperation(value = "设置用户密码", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "设置用户密码")
    @PostMapping("setPassword")
    @ApiResponses({
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 201, message = "非法的密码"),
            @ApiResponse(code = 202, message = "非法的确认密码"),
            @ApiResponse(code = 203, message = "两次输入的密码不一致"),
            @ApiResponse(code = 204, message = "用户设置密码失败"),
            @ApiResponse(code = 1, message = "success")
    })
    public ResponseDTO<String> setPassword(@ApiParam(name = "password", value = "用户密码") String password,
                                           @ApiParam(name = "configPassword", value = "确认密码") String configPassword) {

        Integer userId = UserManager.getUserId();
        if (null == userId || userId <= 0) {
            return ResponseDTO.response(-1, "login fail");
        }
        if (StringUtils.isBlank(password) || StringUtils.length(password) < 6 || StringUtils.length(password) > 20) {
            return ResponseDTO.response(201, "非法的密码");
        }
        if (StringUtils.isBlank(configPassword) || StringUtils.length(configPassword) < 6 || StringUtils.length(configPassword) > 20) {
            return ResponseDTO.response(202, "非法的确认密码");
        }
        if (!StringUtils.equals(password, configPassword)) {
            return ResponseDTO.response(203, "两次输入的密码不一致");
        }

        boolean result = this.userRemoteService.setPassword(userId, password, configPassword);
        if (!Boolean.TRUE.equals(result)) {
            return ResponseDTO.response(204, "用户设置密码失败");
        }
        UserDTO user = this.userRemoteService.getUserById(userId);
        return this.userService.createAuthToken(user.getPhone(), password, null, 0);
    }

    @ApiOperation(value = "更新用户密码", produces = MimeTypeUtils.APPLICATION_JSON_VALUE, notes = "更新用户密码")
    @PostMapping("updatePassword")
    @ApiResponses({
            @ApiResponse(code = -1, message = "login fail"),
            @ApiResponse(code = 201, message = "非法的密码"),
            @ApiResponse(code = 202, message = "非法的确认密码"),
            @ApiResponse(code = 203, message = "两次输入的密码不一致"),
            @ApiResponse(code = 204, message = "非法的旧密码"),
            @ApiResponse(code = 205, message = "用户设置密码失败"),
            @ApiResponse(code = 1, message = "success")
    })
    public ResponseDTO<String> updatePassword(@ApiParam(name = "oldPassword", value = "用户旧密码") String oldPassword,
                                              @ApiParam(name = "newPassword", value = "用户新密码") String newPassword,
                                              @ApiParam(name = "configPassword", value = "确认密码") String configPassword) {

        Integer userId = UserManager.getUserId();
        if (null == userId || userId <= 0) {
            return ResponseDTO.response(-1, "login fail");
        }

        if (StringUtils.isBlank(newPassword) || StringUtils.length(newPassword) < 6 || StringUtils.length(newPassword) > 20) {
            return ResponseDTO.response(201, "非法的密码");
        }
        if (StringUtils.isBlank(configPassword) || StringUtils.length(configPassword) < 6 || StringUtils.length(configPassword) > 20) {
            return ResponseDTO.response(202, "非法的确认密码");
        }
        if (!StringUtils.equals(newPassword, configPassword)) {
            return ResponseDTO.response(203, "两次输入的密码不一致");
        }
        if (StringUtils.isBlank(oldPassword) || StringUtils.length(oldPassword) < 6 || StringUtils.length(oldPassword) > 20) {
            return ResponseDTO.response(204, "非法的旧密码");
        }

        boolean result = this.userRemoteService.updatePassword(userId, oldPassword, newPassword, configPassword);
        if (!Boolean.TRUE.equals(result)) {
            return ResponseDTO.response(205, "用户设置密码失败");
        }
        UserDTO user = this.userRemoteService.getUserById(userId);
        return this.userService.createAuthToken(user.getPhone(), newPassword, null, 0);
    }
}