package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrPro
 * @description: 微信中app信息
 * @date 2019-11-25 16:43:00
 */
public enum WeChatApp {
    
    LAGOU(1, "拉勾公众号"),

    LAGOU_EDU(2, "拉勾教育公众号")
    ;
    
    private int code;
    private String showName;
    private static final Map<Integer, WeChatApp> CACHE = new HashMap<Integer, WeChatApp>();
    
    static {
        for (WeChatApp val :WeChatApp.values()) {
            CACHE.put(val.getCode(), val);
        }
    }
    
    /**
     * 根据code值来转换为枚举类型
     */
    public static WeChatApp parse(int code) {
        return CACHE.get(code);
    }
    
    WeChatApp(int code, String showName) {
        this.code = code;
        this.showName = showName;
    }
    
    public int getCode() {
        return this.code;
    }
    
    public String getShowName() {
        return this.showName;
    }
}
