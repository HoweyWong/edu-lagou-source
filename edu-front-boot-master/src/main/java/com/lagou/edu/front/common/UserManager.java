package com.lagou.edu.front.common;

import com.lagou.edu.common.util.UserContextHolder;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import java.util.Map;

public class UserManager {

    public static final String X_USER_ID = "x-user-id";
    public static final String X_USER_NAME = "x-user-name";
    public static final String X_USER_IP = "x-user-ip";

    /**
     * 获取用户id
     *
     * @return
     */
    public static Integer getUserId() {
        String userId = getItem(X_USER_ID);
        if (StringUtils.isNotBlank(userId) && NumberUtils.isNumber(userId)) {
            return Integer.parseInt(userId);
        }
        return -1;
    }

    private static String getItem(String itemName) {
        UserContextHolder instance = UserContextHolder.getInstance();
        if (null == instance) {
            return null;
        }
        Map<String, String> context = instance.getContext();
        if (MapUtils.isEmpty(context)) {
            return null;
        }
        return context.get(itemName);
    }

    /**
     * 获取用户名称
     *
     * @return
     */
    public static String getUserName() {
        String userName = getItem(X_USER_NAME);
        if (StringUtils.isNotBlank(userName)) {
            return userName;
        }
        return StringUtils.EMPTY;
    }

    public static String getUserIP() {
        return getItem(X_USER_IP);
    }
}
