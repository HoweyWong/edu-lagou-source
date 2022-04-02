package com.lagou.edu.boss.exception;


import com.lagou.edu.common.exception.BaseException;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException() {
        super(OrganizationErrorType.USER_NOT_FOUND);
    }

    public UserNotFoundException(String message) {
        super(OrganizationErrorType.USER_NOT_FOUND, message);
    }
}
