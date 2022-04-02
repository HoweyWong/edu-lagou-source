package com.lagou.edu.course.api.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * (coures状态枚举)   
 * ma wei long
 * 2020年6月18日 上午11:53:45
 */
public enum CourseStatus{

	DRAFT(0,"草稿"),
	PUTAWAY(1,"上架");

    private Integer code;
    private String name;


    CourseStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private static final Map<Integer, CourseStatus> CACHE = new HashMap<Integer, CourseStatus>();

    static {
        for (CourseStatus val :CourseStatus.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static CourseStatus parse(Integer code) {
        return CACHE.get(code);
    }

}
