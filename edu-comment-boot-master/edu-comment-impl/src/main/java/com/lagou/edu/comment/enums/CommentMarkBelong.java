package com.lagou.edu.comment.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 留言归属标记
 */
public enum CommentMarkBelong {

    UN_MARK(0, "无标记"),
    NO_REPLY(1, "无需回复"),
    TEACHER_REPLY(2, "讲师回复"),
    EDITOR_REPLY(3, "编辑回复"),
    OFFICIAL_REPLAY(4, "运营回复");

    private static final Map<Integer, CommentMarkBelong> CACHE = new HashMap<>();

    static {
        for (CommentMarkBelong val : CommentMarkBelong.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    private int code;
    private String showName;

    CommentMarkBelong(int code, String showName) {
        this.code = code;
        this.showName = showName;
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static CommentMarkBelong parse(Integer code) {
        return CACHE.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getShowName() {
        return this.showName;
    }
}
