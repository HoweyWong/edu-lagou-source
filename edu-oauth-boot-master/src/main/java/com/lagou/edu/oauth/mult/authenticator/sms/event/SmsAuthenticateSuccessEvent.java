package com.lagou.edu.oauth.mult.authenticator.sms.event;

import org.springframework.context.ApplicationEvent;

/**
 * 短信认证成功事件
 **/
public class SmsAuthenticateSuccessEvent extends ApplicationEvent {

    public SmsAuthenticateSuccessEvent(Object source) {
        super(source);
    }
}
