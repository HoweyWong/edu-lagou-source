package com.lagou.edu.oauth.mult.authenticator.sms.result;

public class SmsValidateResultContext {

    private static ThreadLocal<SmsCodeValidateResult> holder = new ThreadLocal<>();

    public static void set(SmsCodeValidateResult result) {
        holder.set(result);
    }

    public static SmsCodeValidateResult get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
