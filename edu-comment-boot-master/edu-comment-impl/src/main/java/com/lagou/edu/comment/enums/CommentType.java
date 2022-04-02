package com.lagou.edu.comment.enums;

import java.util.HashMap;
import java.util.Map;

public enum CommentType {
    USER(0, "用户留言"),
    TEACHER(1, "讲师留言"),
    MARKETING(2, "运营留言"),

    TEACHER_REPLY(3, "讲师回复"),
    EDITOR_REPLY(4, "编辑回复"),
    OFFICIAL_REPLAY(5, "官方客服回复");

    private static final Map<Integer, CommentType> CACHE = new HashMap<>();

    static {
        for (CommentType val : CommentType.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    private int code;
    private String showName;

    CommentType(int code, String showName) {
        this.code = code;
        this.showName = showName;
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static CommentType parse(Integer code) {
        return CACHE.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getShowName() {
        return this.showName;
    }
}
