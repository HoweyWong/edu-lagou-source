package com.lagou.edu.front.user.service;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lagou.edu.common.result.ResponseDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RefreshScope
public class UserService {

    @Autowired
    private OAuthRemoteService oAuthRemoteService;
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

    /**
     * 发放access_token、refresh_token
     *
     * @param phone
     * @param password
     * @param code
     * @param type
     * @return
     */
    public ResponseDTO<String> createAuthToken(String phone, String password, String code, Integer type) {
        log.info("phone:{}, password:{}, scope:{}, grantType:{}, clientId:{}, clientSecret:{}", phone, password, scope,
            grantType, clientId, clientSecret);
        String token = null;
        try {
            if (0 == type) {
                token = this.oAuthRemoteService.createToken(phone, password, scope, grantType, clientId, clientSecret,
                    null);
            } else if (1 == type) {
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret,
                    "mobile");
            } else if (2 == type) {
                token = this.oAuthRemoteService.createToken(phone, code, scope, grantType, clientId, clientSecret,
                    "weixin");
            }
            if (StringUtils.isBlank(token)) {
                return ResponseDTO.ofError(206, "登录失败");
            }
            JSONObject jsonObject = JSON.parseObject(token);
            String resultCode = jsonObject.getString("code");
            if (StringUtils.isNotBlank(resultCode)) {
                log.info("phone:{}, jwt token is null, token:{}", phone, token);
                if ("040072".equals(resultCode)) {
                    return ResponseDTO.ofError(207, "验证码错误(" + resultCode + ")");
                }
                return ResponseDTO.ofError(206, "登录失败(" + resultCode + ")");
            }
            String userId = jsonObject.getString("user_id");
            if (StringUtils.isBlank(userId)) {
                return ResponseDTO.response(-1, "login fail", null);
            }
            return ResponseDTO.response(1, "success", token);
        } catch (Exception e) {
            log.error("", e);
            return ResponseDTO.ofError(206, "登录失败");
        }
    }

    public String getRefreshTokenByWeixin(String unionId, String openId) {
        log.info("unionId:{}, openId:{}, scope:{}, grantType:{}, clientId:{}, clientSecret:{}", unionId, openId, scope,
            grantType, clientId, clientSecret);
        try {
            String token = this.oAuthRemoteService.createToken(unionId, openId, scope, grantType, clientId,
                clientSecret, "weixin");
            log.info("unionId:{}, openId:{}, refresh_token:{}", unionId, openId, token);
            if (StringUtils.isBlank(token)) {
                return null;
            }
            JSONObject jsonObject = JSON.parseObject(token);
            return jsonObject.getString("refresh_token");
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }
}
