package com.lagou.edu.message.advice;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.exception.AlertException;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(controller统一异常拦截处理)   
 * @author: ma wei long
 * @date:   2020年6月28日 上午11:53:11
 */
@Slf4j
@RestControllerAdvice
public class ControllerErrAdvice {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月28日 上午11:53:22   
	*/
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseDTO<?> handleException(Exception e, HttpServletRequest request) {
        log.error("handleException - url：{} requestParam:{} errMsg:{}",request.getRequestURI(),JSON.toJSONString(request.getParameterMap()), e);
        if(e instanceof AlertException) {
        	return ResponseDTO.ofError(((AlertException) e).getCode(),e.getMessage());
        }
        return ResponseDTO.ofError(ResultCode.SERVER_ERROR.getMessage());
    }
}
