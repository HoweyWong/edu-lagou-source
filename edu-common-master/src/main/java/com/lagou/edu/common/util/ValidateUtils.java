package com.lagou.edu.common.util;

import java.util.Collection;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import com.lagou.edu.common.exception.AlertException;
import com.lagou.edu.common.result.ResultCode;


/**
 * (校验判断工具 ， 可以消除部分if判断)   
 *  ma wei long
 * 2020年6月17日 下午7:49:54
 */
public class ValidateUtils {

    public static void isTrue(Boolean value, int code, String msg) {
        if (!Boolean.TRUE.equals(value)) {
            throw new AlertException(code, msg);
        }
    }
    
    public static void isTrue(Boolean value,String msg) {
        if (!Boolean.TRUE.equals(value)) {
            throw new AlertException(ResultCode.ALERT_ERROR.getState(), msg);
        }
    }

    public static void isTrue(Boolean value, ResultCode respCode) {
        if (!Boolean.TRUE.equals(value)) {
            throw new AlertException(respCode.getState(), respCode.getMessage());
        }
    }

    public static void isFalse(Boolean value, int code, String msg) {
        if (!Boolean.FALSE.equals(value)) {
            throw new AlertException(code, msg);
        }
    }
    
    public static void isFalse(Boolean value, String msg) {
        if (!Boolean.FALSE.equals(value)) {
            throw new AlertException(ResultCode.ALERT_ERROR.getState(), msg);
        }
    }

    public static void isFalse(Boolean value, ResultCode respCode) {
        if (!Boolean.FALSE.equals(value)) {
            throw new AlertException(respCode.getState(), respCode.getMessage());
        }
    }

    public static void notEmpty(String value, int code, String msg) {
        if (value == null || value.isEmpty()) {
            throw new AlertException(code, msg);
        }
    }
    public static void notEmpty(String value, ResultCode respCode) {
        if (value == null || value.isEmpty()) {
            throw new AlertException(respCode.getState(), respCode.getMessage());
        }
    }

    public static void notBlank(String value, int code, String msg) {
        if (StringUtils.isBlank(value)) {
            throw new AlertException(code, msg);
        }
    }

    public static void notBlank(String value, String msg) {
        if (StringUtils.isBlank(value)) {
            throw new AlertException(ResultCode.PARAM_ERROR.getState(), msg);
        }
    }

    public static void notNullParam(Object value) {
        if (value == null) {
            throw new AlertException(ResultCode.PARAM_ERROR.getState(), "参数错误", "不能为空");
        }
    }

    public static void notNull(Object value, int code, String msg) {
        if (value == null) {
            throw new AlertException(code, msg);
        }
    }

    public static void notNullParam(Object value, String paramName) {
        if (value == null) {
            throw new AlertException(ResultCode.PARAM_ERROR.getState(), "参数错误", paramName);
        }
    }

    
    public static void isNull(Object value, int code, String msg) {
        if (value != null) {
            throw new AlertException(code, msg);
        }
    }

    public static void notEmptyParam(Collection value, String paramName) {
        if (value == null || value.isEmpty()) {
            throw new AlertException(ResultCode.PARAM_ERROR.getState(), "参数错误", paramName);
        }
    }

    public static void isEmpty(Collection value, int code, String msg) {
        if (value != null && !value.isEmpty()) {
            throw new AlertException(code, msg);
        }
    }

    public static <T> boolean isEquals(T expected, T actual) {
        return expected == null ? actual == null : expected.equals(actual);
    }

    public static interface CompareCallback {
        void beforeThrow();
    }
}
