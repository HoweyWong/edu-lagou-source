package com.lagou.edu.front.advice;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.klock.handler.KlockTimeoutException;
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
 * @date:   2020年6月17日 下午3:56:17
*/
@Slf4j
@RestControllerAdvice
public class ControllerErrAdvice {
	
	/**
	 * @param: @param e
	 * @param: @param request
	 * @author: ma wei long
	 * @date:   2020年6月17日 下午4:11:56   
	 */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    ResponseDTO<?> handleException(Exception e, HttpServletRequest request) {
    	log.error("handleException - url：{} requestParam:{} errMsg:{}",request.getRequestURI(),JSON.toJSONString(request.getParameterMap()), e);
        if(e instanceof AlertException) {
        	return ResponseDTO.ofError(((AlertException) e).getCode(),e.getMessage());
        }
        if(e instanceof KlockTimeoutException) {
        	return ResponseDTO.ofError(ResultCode.REPETITION_ERROR.getState(),ResultCode.REPETITION_ERROR.getMessage());
        }
        return ResponseDTO.ofError(ResultCode.SERVER_ERROR.getMessage());
    }
}
