package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lixiangyong
 * @Description 支付状态
 * @Date: 2018/11/8 10:54 AM
 **/
public enum Status implements Common{

    INVALID(-1,"订单失效"),
    NOT_PAY(1,"未支付"),
    PAY_SUCCESS(2,"支付成功"),
    PAY_FAILED(3,"支付失败");

    private Integer code;
    private String name;


    Status(Integer code, String name) {
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

    private static final Map<Integer, Status> CACHE = new HashMap<Integer, Status>();

    static {
        for (Status val :Status.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static Status parse(Integer code) {
        return CACHE.get(code);
    }

}
