package com.lagou.edu.order.api.enums;

/**
 * @author: ma wei long
 * @date:   2020年6月21日 下午11:58:15
 */
public enum StatusTypeEnum {
    INSERT("保存"),
    UPDATE("更新"),
    CANCEL("超时取消"),
    ;
    public final String desc;
    StatusTypeEnum(String desc) {
        this.desc = desc;
    }
}
