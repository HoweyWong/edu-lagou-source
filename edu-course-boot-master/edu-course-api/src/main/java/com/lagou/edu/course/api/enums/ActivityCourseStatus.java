package com.lagou.edu.course.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author: ma wei long
 * @date:   2020年7月7日 下午8:39:14
 */
public enum ActivityCourseStatus {

	UN_PUB(0,"未上架"),
	PUB(10,"已上架");

    private Integer code;
    private String name;


    ActivityCourseStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, ActivityCourseStatus> CACHE = new HashMap<Integer, ActivityCourseStatus>();

    static {
        for (ActivityCourseStatus val :ActivityCourseStatus.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static ActivityCourseStatus parse(Integer code) {
        return CACHE.get(code);
    }
}
