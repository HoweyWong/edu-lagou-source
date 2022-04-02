package com.lagou.edu.pay.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: lixiangyong
 * @Description 支付渠道
 * @Date: 2018/11/8 10:54 AM
 **/
public enum Channel implements Common{

    WECHAT(1,"weChat"),
    ALIPAY(2,"aliPay"),
    ;

    private Integer code;
    private String name;

    Channel(Integer code, String name) {
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

    private static final Map<String, Channel> CACHE = new HashMap<String, Channel>();

    static {
        for (Channel val :Channel.values()) {
            CACHE.put(val.getCode().toString(), val);
            CACHE.put(val.getName(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static Channel parse(Integer code) {
        return CACHE.get(code.toString());
    }
    
    /**
     * 根据name值来转换为枚举类型
     */
    public static Channel ofName(String name) {
        return CACHE.get(name);
    }

}
