package com.lagou.edu.oauth.mult.authenticator.sms.result;

import com.lagou.edu.oauth.exception.AuthErrorType;
import lombok.Data;

@Data
public class SmsCodeValidateResult {
    private boolean success = true;
    private AuthErrorType authErrorType;
}
