package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lixiangyong
 * @Description 币种
 * @Date: 2018/11/8 10:54 AM
 **/
public enum Currency implements Common {

    CNY(1, "cny"),
    GBEANS(2, "gBeans");

    private Integer code;
    private String name;


    Currency(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, Currency> CACHE = new HashMap<Integer, Currency>();

    static {
        for (Currency val : Currency.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static Currency parse(Integer code) {
        return CACHE.get(code);
    }

}
