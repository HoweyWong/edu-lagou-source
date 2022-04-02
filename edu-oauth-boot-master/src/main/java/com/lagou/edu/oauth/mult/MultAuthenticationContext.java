package com.lagou.edu.oauth.mult;

public class MultAuthenticationContext {

    private static ThreadLocal<MultAuthentication> holder = new ThreadLocal<>();

    public static void set(MultAuthentication multAuthentication) {
        holder.set(multAuthentication);
    }

    public static MultAuthentication get() {
        return holder.get();
    }

    public static void clear() {
        holder.remove();
    }
}
