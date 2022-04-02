package com.lagou.edu.order.api.enums;

import java.util.HashMap;
import java.util.Map;
/**
 * @Description:(订单来源)   
 * @author: ma wei long
 * @date:   2020年6月19日 上午10:35:56
*/
public enum UserCourseOrderSourceType{
    USER_BUY(1,"用户下单购买"),
    OFFLINE_BUY(2,"后台添加专栏");

    private Integer code;
    private String name;


    UserCourseOrderSourceType(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, UserCourseOrderSourceType> CACHE = new HashMap<Integer, UserCourseOrderSourceType>();

    static {
        for (UserCourseOrderSourceType val :UserCourseOrderSourceType.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static UserCourseOrderSourceType parse(Integer code) {
        return CACHE.get(code);
    }

}
