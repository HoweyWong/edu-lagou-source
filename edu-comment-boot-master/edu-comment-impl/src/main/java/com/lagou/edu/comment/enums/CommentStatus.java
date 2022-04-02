package com.lagou.edu.comment.enums;

import java.util.HashMap;
import java.util.Map;


public enum CommentStatus {
    INIT(0, "待审核"),
    PASS(1, "审核通过"),
    NO_PASS(2, "审核不通过"),
    DELETE(3, "已删除");

    private static final Map<Integer, CommentStatus> CACHE = new HashMap<>();

    static {
        for (CommentStatus val : CommentStatus.values()) {
            CACHE.put(val.getCode(), val);
        }
    }

    private int code;
    private String showName;

    CommentStatus(int code, String showName) {
        this.code = code;
        this.showName = showName;
    }

    /**
     * 根据code值来转换为枚举类型
     */
    public static CommentStatus parse(Integer code) {
        return CACHE.get(code);
    }

    public int getCode() {
        return this.code;
    }

    public String getShowName() {
        return this.showName;
    }
    }
