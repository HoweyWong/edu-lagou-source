package com.lagou.edu.order.advice;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.order.annotation.UserCourseOrderRecord;
import com.lagou.edu.order.api.enums.UserCourseOrderStatus;
import com.lagou.edu.order.entity.UserCourseOrder;
import com.lagou.edu.order.service.IUserCourseOrderRecordService;

import lombok.extern.slf4j.Slf4j;


/**
 * @author: ma wei long
 * @date:   2020年6月21日 下午11:59:52
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class UserCourseOrderRecordAdvice {
	
    @Autowired
	private IUserCourseOrderRecordService userCourseOrderRecordService;

    @Pointcut("@annotation(com.lagou.edu.order.annotation.UserCourseOrderRecord)")
    private void annotationUserCourseOrderRecord() {}

    @AfterReturning(pointcut= "annotationUserCourseOrderRecord()",returning = "rvt")
    public void intercept(JoinPoint joinPoint,Object rvt) throws Throwable {
    	CompletableFuture.runAsync(() -> {
    		Object[] args = null;
    		try {
    			args = joinPoint.getArgs();
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                UserCourseOrderRecord userCourseOrderRecord = method.getAnnotation(UserCourseOrderRecord.class);
                if(null == userCourseOrderRecord) {
                	return;
                }
                if(null == userCourseOrderRecord.type()) {
                	log.error("userCourseOrderRecord.type() is null request:{} response:{}",JSON.toJSONString(args),JSON.toJSONString(rvt));
                	return;
                }
                switch (userCourseOrderRecord.type()) {
    			case INSERT:
    				UserCourseOrder order = (UserCourseOrder) args[0];
    				userCourseOrderRecordService.save(buildUserCourseOrderRecord(order.getOrderNo(), null, UserCourseOrderStatus.CREATE.getCode()));
    				break;
    			case UPDATE:
    				String orderNo = (String) args[0];
    				Integer status = (Integer) args[1];
    				userCourseOrderRecordService.save(buildUserCourseOrderRecord(orderNo, UserCourseOrderStatus.CREATE.getCode(),status));
    				break;
    			default:
    				log.error("userCourseOrderRecord.type:{} is error request:{} response:{}",userCourseOrderRecord.type(),JSON.toJSONString(args),JSON.toJSONString(rvt));
    				return;
    			}
			} catch (Exception e) {
				log.error("error - request:{} response:{} error:",JSON.toJSONString(args),JSON.toJSONString(rvt),e);
			}
        });
    }
    
    /**
     * @Description: (构建UserCourseOrderRecord信息)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午2:25:19   
     */
    com.lagou.edu.order.entity.UserCourseOrderRecord buildUserCourseOrderRecord(String orderNo,Integer fromStatus,Integer toStatus){
    	com.lagou.edu.order.entity.UserCourseOrderRecord saveUserCourseOrderRecord = new com.lagou.edu.order.entity.UserCourseOrderRecord();
		saveUserCourseOrderRecord.setCreateTime(new Date());
		saveUserCourseOrderRecord.setCreateUser("auto");
		saveUserCourseOrderRecord.setOrderNo(orderNo);
		saveUserCourseOrderRecord.setFromStatus(null == fromStatus ? null : String.valueOf(fromStatus));
		saveUserCourseOrderRecord.setToStatus(String.valueOf(toStatus));
		saveUserCourseOrderRecord.setUpdateTime(saveUserCourseOrderRecord.getCreateTime());
		saveUserCourseOrderRecord.setUpdateUser("auto");
    	return saveUserCourseOrderRecord;
    }
}
