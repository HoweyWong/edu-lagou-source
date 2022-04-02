package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lixiangyong
 * @Description 支付类型
 * @Date: 2018/11/8 10:54 AM
 **/
public enum OrderType implements Common{

    BUY_COURSE(1,"购买课程"),
    RECHARGE(2,"充值");

    private Integer code;
    private String name;


    OrderType(Integer code, String name) {
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

    private static final Map<Integer, OrderType> CACHE = new HashMap<Integer, OrderType>();

    static {
        for (OrderType val :OrderType.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static OrderType parse(Integer code) {
        return CACHE.get(code);
    }
}
