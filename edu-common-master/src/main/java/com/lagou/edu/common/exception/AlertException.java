package com.lagou.edu.common.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * (提示性错误)   
 * ma wei long
 * 2020年6月17日 下午7:47:26
 */
@Getter
@Setter
public class AlertException extends RuntimeException {

    private static final long serialVersionUID = -4743819198578737692L;

    private int code = 400;
    private String error;

    public AlertException(int code, String message) {
        super(message);
        this.code = code;
    }

    public AlertException(int code, String message, String error) {
        super(message);
        this.code = code;
        this.error = error;
    }

    public AlertException() {
        super();
    }

    public AlertException(String message) {
        super(message);
    }

    public AlertException(String message, Throwable cause) {
        super(message, cause);
    }

    public AlertException(Throwable cause) {
        super(cause);
    }
}
