package com.lagou.edu.oauth.mult.authenticator.sms;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.entity.vo.Result;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.oauth.exception.AuthErrorType;
import com.lagou.edu.oauth.mult.MultAuthentication;
import com.lagou.edu.oauth.mult.authenticator.AbstractPreparableMultAuthenticator;
import com.lagou.edu.oauth.mult.authenticator.sms.event.SmsAuthenticateBeforeEvent;
import com.lagou.edu.oauth.mult.authenticator.sms.event.SmsAuthenticateSuccessEvent;
import com.lagou.edu.oauth.mult.authenticator.sms.exception.SmsValidateException;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsCodeValidateResult;
import com.lagou.edu.oauth.mult.authenticator.sms.result.SmsValidateResultContext;
import com.lagou.edu.user.api.UserRemoteService;
import com.lagou.edu.user.api.VerificationCodeRemoteService;
import com.lagou.edu.user.api.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 短信验证码集成认证
 **/
@Slf4j
@Component
public class SmsMultAuthenticator extends AbstractPreparableMultAuthenticator implements ApplicationEventPublisherAware {

    @Autowired
    private UserRemoteService userRemoteService;

    @Autowired
    private VerificationCodeRemoteService verificationCodeRemoteService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private ApplicationEventPublisher applicationEventPublisher;

    private final static String SMS_AUTH_TYPE = "mobile";

    @Override
    public UserDTO authenticate(MultAuthentication multAuthentication) {
        SmsCodeValidateResult validateResult = SmsValidateResultContext.get();
        if (null != validateResult && !validateResult.isSuccess()) {
            AuthErrorType authErrorType = validateResult.getAuthErrorType();
            log.info("短信验证码错误,手机号:{}, 结果:{}", multAuthentication.getUsername(), authErrorType);
//            throw new OAuth2Exception(authErrorType.getMesg());
            return null;
        }

        //获取密码，实际值是验证码
        String password = multAuthentication.getAuthParameter("password");
        //获取用户名，实际值是手机号
        String username = multAuthentication.getUsername();
        //发布事件，可以监听事件进行自动注册用户
        this.applicationEventPublisher.publishEvent(new SmsAuthenticateBeforeEvent(multAuthentication));
        //通过手机号码查询用户
        UserDTO userDTO = this.userRemoteService.getUserByPhone(username);
        if (userDTO != null) {
            //将密码设置为验证码
            userDTO.setPassword(passwordEncoder.encode(password));
            //发布事件，可以监听事件进行消息通知
            this.applicationEventPublisher.publishEvent(new SmsAuthenticateSuccessEvent(multAuthentication));
        }
        return userDTO;
    }

    @Override
    public void prepare(MultAuthentication multAuthentication, HttpServletResponse response) {

        String smsCode = multAuthentication.getAuthParameter("password");
        String username = multAuthentication.getAuthParameter("username");
        ResponseDTO result = verificationCodeRemoteService.checkCode(username, smsCode);
        if (null == result || !result.isSuccess()) {

            SmsCodeValidateResult validateResult = new SmsCodeValidateResult();
            validateResult.setSuccess(false);
            validateResult.setAuthErrorType(AuthErrorType.ERROR_VERIFY_CODE);
//            SmsValidateResultContext.set(validateResult);
//            // 直接写回响应
////            ResponseEntity.status(HttpStatus.OK).body(AuthErrorType.ERROR_VERIFY_CODE);
//            PrintWriter writer = null;
//            try {
//                writer = response.getWriter();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            response.setContentType("application/json;charset=UTF-8");
//            response.setCharacterEncoding("UTF-8");
//            Result fail = Result.fail(AuthErrorType.valueOf(AuthErrorType.ERROR_VERIFY_CODE.getMesg().toUpperCase()), AuthErrorType.ERROR_VERIFY_CODE);
//            writer.write(JSON.toJSONString(fail));
//            writer.flush();
//            writer.close();

            throw new SmsValidateException(validateResult);
        }
    }

    @Override
    public boolean support(MultAuthentication multAuthentication) {
        return SMS_AUTH_TYPE.equals(multAuthentication.getAuthType());
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
