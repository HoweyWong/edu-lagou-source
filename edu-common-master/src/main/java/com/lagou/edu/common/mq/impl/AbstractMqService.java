package com.lagou.edu.common.mq.impl;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.mq.RocketMqService;
import com.lagou.edu.common.mq.dto.BaseMqDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月27日 下午2:04:16
 */
@Slf4j
public abstract class AbstractMqService implements RocketMqService {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午12:02:34   
	*/
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    abstract public void convertAndSend(String topic,BaseMqDTO<?> data);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午12:02:34   
	*/
    @Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    abstract public void sendDelayed(String topic,BaseMqDTO<?> data,int delayLevel);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午12:02:34   
	*/
    @Recover
    public void recover(Exception ex, Object arg0,Object arg1) {
    	//TODO ma wei long 后续可以考虑持久化&报警
        log.error("AbstractMqService - recover - args0:{} arg1:{} ex",JSON.toJSONString(arg0),JSON.toJSONString(arg1),ex);
    }
}
