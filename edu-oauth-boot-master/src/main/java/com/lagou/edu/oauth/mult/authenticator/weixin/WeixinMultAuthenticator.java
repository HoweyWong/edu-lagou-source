package com.lagou.edu.oauth.mult.authenticator.weixin;

import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.UserWeixinRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import com.lagou.edu.user.api.dto.WeixinDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;

/**
 * 微信开放平台登录
 **/
@Slf4j
@Component
public class WeixinMultAuthenticator extends AbstractPreparableMultAuthenticator {

    @Autowired
    private UserRemoteService userRemoteService;
    @Autowired
    private UserWeixinRemoteService userWeixinRemoteService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final static String WEIXIN_AUTH_TYPE = "weixin";

    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {
        //获取密码，实际值是openId
        String openId = multAuthentication.getAuthParameter("password");
        WeixinDTO weixinByOpenId = this.userWeixinRemoteService.getUserWeixinByOpenId(openId);
        if (null == weixinByOpenId) {
            return null;
        }

        //通过用户id查询用户
        UserDTO userDTO = this.userRemoteService.getUserById(weixinByOpenId.getUserId());
        if (null == userDTO) {
            return null;
        }
        //将密码设置为验证码
        userDTO.setPassword(passwordEncoder.encode(openId));
        return userDTO;
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return WEIXIN_AUTH_TYPE.equals(multAuthentication.getAuthType());
    }
}
