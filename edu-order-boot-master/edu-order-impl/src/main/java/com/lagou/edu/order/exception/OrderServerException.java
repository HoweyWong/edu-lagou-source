package com.lagou.edu.order.exception;

/**
 * @author: ma wei long
 * @date:   2020年7月1日 下午12:05:46
 */
public class OrderServerException extends RuntimeException {

	/**
	 */
	private static final long serialVersionUID = -4306328050844160674L;
	
	String code = "400";
    String error;
    Boolean bizException = true;

    public OrderServerException(String code, String message) {
        super(message);
        this.code = code;
    }
    public OrderServerException(String code,String message,Boolean bizException) {
        super(message);
        this.code = code;
        this.bizException = bizException;
    }

    public OrderServerException(String code, String message, String error) {
        super(message);
        this.code = code;
        this.error = error;
    }

    public OrderServerException(String code, String message, String error, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.error = error;
    }

    public OrderServerException() {
        super();
    }

    public OrderServerException(String message) {
        super(message);
    }

    public OrderServerException(String message, Throwable cause) {
        super(message, cause);
    }
    public OrderServerException(String message, Throwable cause,Boolean bizException) {
        super(message, cause);
        this.bizException = bizException;
    }

    public OrderServerException(Throwable cause) {
        super(cause);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
	public Boolean getBizException() {
		return bizException;
	}
	public void setBizException(Boolean bizException) {
		this.bizException = bizException;
	}
}
