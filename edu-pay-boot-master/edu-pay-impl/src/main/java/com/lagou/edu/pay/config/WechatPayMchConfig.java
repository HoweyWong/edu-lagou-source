package com.lagou.edu.pay.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

/**
 * 微信支付PC端配置
 * @author: ma wei long
 * @date:   2020年7月13日 下午5:34:13
*/
@Data
@ConfigurationProperties(prefix = "pay.wechatpay.pc")
@Component
public class WechatPayMchConfig {
	private String appId;
	private String mchId;
    private String privateKey;
    private String publicKey;
    private String payHost;
    private String notifyUrl;
    public Boolean useFakeMoney;
}
