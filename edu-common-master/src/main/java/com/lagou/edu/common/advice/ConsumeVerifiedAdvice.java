package com.lagou.edu.common.advice;
import java.lang.reflect.Method;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.annotation.ConsumeVerified;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.util.ValidateUtils;

import lombok.extern.slf4j.Slf4j;


/**
 * @author: ma wei long
 * @date:   2020年7月28日 下午8:58:00
 */
@Slf4j
@Aspect
@Component
@Order(Ordered.LOWEST_PRECEDENCE)
public class ConsumeVerifiedAdvice {
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;

    @Pointcut("@annotation(com.lagou.edu.common.annotation.ConsumeVerified)")
    private void consumeVerified() {}

    @Around("consumeVerified()")
    public void intercept(ProceedingJoinPoint joinPoint) throws Throwable {
    	Object[] args = joinPoint.getArgs();
    	log.info("mq - data:{}",JSON.toJSONString(args));
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        ConsumeVerified consumeVerified = method.getAnnotation(ConsumeVerified.class);
        ValidateUtils.isTrue(null != consumeVerified, "ConsumeVerified is null");
        Object o = args[0];
        ValidateUtils.isTrue(o != consumeVerified, "args is null");

        BaseMqDTO<?> data = (BaseMqDTO<?>)o;
        String messageId = data.getMessageId();
        ValidateUtils.isTrue(null != messageId, "messageId is null");

        if(redisTemplate.opsForHash().hasKey("edu_mq",messageId)) {
        	log.warn("ConsumeVerifiedAdvice - 消息重复消费：data:{}",JSON.toJSONString(data));
			return;
		}
        
    	joinPoint.proceed();
    	
    	redisTemplate.opsForHash().put("edu_mq",messageId, messageId);
    	//TODO ma weilong  数据多了可以设置个比较长的有效期  或者持久化到 其他地方 或者定期清理下数据
    	//redisTemplate.expire("edu_mq", 30, TimeUnit.DAYS);
    }
}
