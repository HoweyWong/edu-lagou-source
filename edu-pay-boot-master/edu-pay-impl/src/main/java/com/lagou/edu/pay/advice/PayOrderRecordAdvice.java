package com.lagou.edu.pay.advice;
import java.lang.reflect.Method;
import java.math.BigDecimal;
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
import com.lagou.edu.pay.annotation.PayOrderRecord;
import com.lagou.edu.pay.api.enums.OperationType;
import com.lagou.edu.pay.api.enums.Status;
import com.lagou.edu.pay.entity.PayOrder;
import com.lagou.edu.pay.service.IPayOrderRecordService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 上午12:28:43
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class PayOrderRecordAdvice {
	
    @Autowired
	private IPayOrderRecordService payOrderRecordService;

    @Pointcut("@annotation(com.lagou.edu.pay.annotation.PayOrderRecord)")
    private void annotationPayOrderRecord() {}

    @AfterReturning(pointcut= "annotationPayOrderRecord()",returning = "rvt")
    public void intercept(JoinPoint joinPoint,Object rvt) throws Throwable {
    	CompletableFuture.runAsync(() -> {
    		Object[] args = null;
    		try {
    			args = joinPoint.getArgs();
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                PayOrderRecord payOrderRecord = method.getAnnotation(PayOrderRecord.class);
                if(null == payOrderRecord) {
                	return;
                }
                if(null == payOrderRecord.type()) {
                	log.error("payOrderRecord.type() is null request:{} response:{}",JSON.toJSONString(args),JSON.toJSONString(rvt));
                	return;
                }
                PayOrder order = null;
                switch (payOrderRecord.type()) {
    			case INSERT:
    				order = (PayOrder)rvt;
    				payOrderRecordService.save(buildPayOrderRecord(order.getOrderNo(), order.getAmount(), null, Status.NOT_PAY.getCode(), OperationType.CREATE));
    				break;
    			case UPDATE:
    				order = (PayOrder) args[0];
    				payOrderRecordService.save(buildPayOrderRecord(order.getOrderNo(), order.getAmount(), Status.NOT_PAY.getCode(), order.getStatus(), OperationType.PAY));
    				break;
    			case CANCEL:
    				order = (PayOrder) args[0];
    				payOrderRecordService.save(buildPayOrderRecord(order.getOrderNo(), order.getAmount(), Status.NOT_PAY.getCode(), Status.INVALID.getCode(), OperationType.PAY));
    				break;
    			default:
    				log.error("payOrderRecord.type:{} is error request:{} response:{}",payOrderRecord.type(),JSON.toJSONString(args),JSON.toJSONString(rvt));
    				return;
    			}
			} catch (Exception e) {
				log.error("error - request:{} response:{} error:",JSON.toJSONString(args),JSON.toJSONString(rvt),e);
			}
        });
    }
    
    /**
     * @Description: (构建PayOrderRecord数据)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午2:15:41   
     */
    com.lagou.edu.pay.entity.PayOrderRecord buildPayOrderRecord(String orderNo,BigDecimal amount,Integer fromStatus,Integer toStatus,OperationType type) {
    	com.lagou.edu.pay.entity.PayOrderRecord savePayOrderRecord = new com.lagou.edu.pay.entity.PayOrderRecord();
		savePayOrderRecord.setCreatedAt(new Date());
    	savePayOrderRecord = new com.lagou.edu.pay.entity.PayOrderRecord();
		savePayOrderRecord.setCreatedAt(new Date());
		savePayOrderRecord.setCreatedBy("auto");
		savePayOrderRecord.setOrderNo(orderNo);
		savePayOrderRecord.setFromStatus(null == fromStatus ? null : String.valueOf(fromStatus));
		savePayOrderRecord.setPaidAmount(amount.multiply(new BigDecimal(100)).intValue());
		savePayOrderRecord.setToStatus(String.valueOf(toStatus));
		savePayOrderRecord.setType(type.name());
		return savePayOrderRecord;
    }
}
