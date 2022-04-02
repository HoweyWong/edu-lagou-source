package com.lagou.edu.oauth.oauth2.enhancer;

import com.google.common.collect.Maps;
import com.lagou.edu.oauth.entity.UserJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.Map;

/**
 * 自定义token携带内容
 */
@Slf4j
public class CustomTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = Maps.newHashMap();
        // 自定义token内容，加入组织机构信息
        additionalInfo.put("organization", authentication.getName());
        try {
            // 自定义token内容，加入userId
            UserJwt details = (UserJwt) authentication.getPrincipal();
            if (null != details) {
                additionalInfo.put("user_id", details.getId());
            }
        } catch (Exception e) {
            log.error("user name:{}", authentication.getName());
        }
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}