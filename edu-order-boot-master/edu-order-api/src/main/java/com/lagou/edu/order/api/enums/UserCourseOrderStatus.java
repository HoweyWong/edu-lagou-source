package com.lagou.edu.order.api.enums;

import java.util.HashMap;
import java.util.Map;
/**
 * @Description:(商品订单状态枚举)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午4:58:27
 */
public enum UserCourseOrderStatus{
    CREATE(0,"已创建"),
    PAID(10,"已支付"),
    SUCCESS(20,"已完成"),
    CANCEL(30,"已取消"),
    EXPIRED(40,"已过期");

    private Integer code;
    private String name;


    UserCourseOrderStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, UserCourseOrderStatus> CACHE = new HashMap<Integer, UserCourseOrderStatus>();

    static {
        for (UserCourseOrderStatus val :UserCourseOrderStatus.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static UserCourseOrderStatus parse(Integer code) {
        return CACHE.get(code);
    }

}
