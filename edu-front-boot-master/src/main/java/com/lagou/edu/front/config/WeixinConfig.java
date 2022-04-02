package com.lagou.edu.front.config;

import lombok.Data;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.api.impl.WxMpUserServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@RefreshScope
public class WeixinConfig {
    @Value("${wx.mp.config.appId}")
    private String appId;
    @Value("${wx.mp.config.secret}")
    private String secret;
    @Value("${wx.mp.config.token}")
    private String token;
    @Value("${wx.mp.config.aesKey}")
    private String aesKey;

    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        // 设置微信公众号的appid
        config.setAppId(getAppId());
        // 设置微信公众号的app corpSecret
        config.setSecret(getSecret());
        // 设置微信公众号的token
        config.setToken(getToken());
        // 设置消息加解密密钥
        config.setAesKey(getAesKey());

        WxMpService wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(config);
        return wxMpService;
    }

    @Bean
    public WxMpUserService wxMpUserService() {
        WxMpUserService wxMpService = new WxMpUserServiceImpl(wxMpService());
        return wxMpService;
    }
}