package com.lagou.edu.message.advice;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.exception.AlertException;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.util.EnvironmentUtils;
import com.lagou.edu.message.exception.MessageServerException;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月1日 上午11:55:47
 */
@Slf4j
@Aspect
@Component
public class MessageServerRemoteServiceAdvice {
	
	public static final String ERROR_MSG = "服务器 开小差了 请您稍后重试";

    @Pointcut("execution(* com.lagou.edu.message.controller..*.*(..))")
    public void messageServerRemoteServiceFacade() {}
    
    public static String[] filterMethod = {"IndexController"};//过滤方法
    
    /**
     * @author: ma wei long
     * @date:   2020年7月1日 下午12:55:01   
    */
	@Around("messageServerRemoteServiceFacade()")
    public Object intercept(ProceedingJoinPoint joinPoint) throws Throwable {
    	long nanoTime = System.nanoTime();//开始时间
        String requestParam = getRequestParam(joinPoint);
        String apiMethod = StringUtils.join(joinPoint.getTarget().getClass().getName(),".",joinPoint.getSignature().getName());
        if(filterMethod(apiMethod)) {
        	return joinPoint.proceed();
        }
        ResponseDTO<?> res = null;
        boolean isProduction = EnvironmentUtils.isProd();
        try {
            res = (ResponseDTO<?>)joinPoint.proceed();
        } catch (IllegalArgumentException e) {
            log.error("[IllegalArgumentException]异常 {}", e);
            throw isProduction ? new MessageServerException(ERROR_MSG, e,false) : e;
        } catch (AlertException e) {
            log.error("[AlertException]异常 {}",e);
            throw isProduction ? new AlertException(e.getCode(),ERROR_MSG, e.getMessage()) : e;
        }  catch (NullPointerException e) {
            log.error("[NullPointerException]异常 {}",e);
            throw isProduction ? new MessageServerException(ERROR_MSG, e) : e;
        } catch (MessageServerException e) {
            log.error("[OrderServerException]异常 {}",e);
            throw isProduction ? new MessageServerException(ERROR_MSG, e) : e;
        } catch (Exception e) {
            log.error("[Exception]异常 {}",e);
            throw isProduction ? new MessageServerException(ERROR_MSG,e,false) : e;
        } catch (Throwable e) {
            log.error("[Throwable]异常 {}",e);
            throw isProduction ? new MessageServerException(ERROR_MSG,e,false) : e;
        } finally {
            String responseParam = "- 响应结果:" + JSON.toJSONString( JSON.toJSONString(res));
            Long time = (System.nanoTime() - nanoTime) / 1000000;
            log.info("MessageServerRemoteServiceAdvice - 请求接口名称：【{}】  请求参数:" + requestParam + ";"  + responseParam + " - 耗时:{}ms {}",apiMethod,time,(time >= 10000) ? "invokeTimeOut" : "");
        }
        return res;
    }
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月1日 下午10:12:37   
	*/
	private static boolean filterMethod(String method) {
    	for(int i = 0; i < filterMethod.length; i++) {
    		if(method.contains(filterMethod[i])) {
            	return true;
            }
    	}
    	return false;
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月1日 下午12:57:31   
	*/
	private String getRequestParam(ProceedingJoinPoint joinPoint) {
		try {
			Object[] args = joinPoint.getArgs();
	        String[] argNames = ((MethodSignature)joinPoint.getSignature()).getParameterNames();
	        StringBuilder sb = new StringBuilder();
	        if(null != argNames && argNames.length > 0) {
	            for(int i = 0; i < argNames.length; i++) {
	            	sb.append(argNames[i]).append(":").append(args[i] instanceof HttpServletResponse ? "" : JSON.toJSONString(args[i]));
	            	if(i != argNames.length - 1) {
	            		sb.append(",");
	            	}
	            }
	        }
	        return sb.toString();
		} catch (Exception e) {
			log.error("getRequestParam - error",e);
		}
		return null;
	}
}
