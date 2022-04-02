package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MrPro
 * @description: 支付平台
 * @date 2019-11-05 17:47:00
 */
public enum Platform implements Common {

    /**
     * 拉勾平台默认的
     */
    LAGOU(1, "拉勾网"),

    /**
     * 拉勾教育
     */
    LAGOU_EDU(2, "拉勾教育")

    ;

    private int code;
    private String showName;
    private static final Map<Integer, Platform> CACHE = new HashMap<Integer, Platform>();

    static {
        for (Platform val :Platform.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static Platform parse(int code) {
        return CACHE.get(code);
    }

    Platform(int code, String showName) {
        this.code = code;
        this.showName = showName;
    }

    public Integer getCode() {
        return this.code;
    }

    @Override
    public String getName() {
        return this.showName;
    }

}
