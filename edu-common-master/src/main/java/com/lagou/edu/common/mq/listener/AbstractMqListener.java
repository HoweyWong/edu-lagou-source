package com.lagou.edu.common.mq.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月27日 下午2:37:35
 */
@Slf4j
public abstract class AbstractMqListener<T>{
    
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午9:04:18   
	*/
	public boolean checkMessageId(String messageId) {
		if(redisTemplate.opsForHash().hasKey("edu_mq",messageId)) {
			return true;
		}
		return false;
	}
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午9:06:40   
	*/
	public void updateMessageId(String messageId) {
		redisTemplate.opsForHash().put("edu_mq",messageId, messageId);
		//TODO ma weilong  数据多了可以设置个比较长的有效期  或者持久化到 其他地方 或者定期清理下数据
    	//redisTemplate.expire("edu_mq", 30, TimeUnit.DAYS);
	}
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午2:37:35
	 */
	@Retryable(value = {Exception.class}, maxAttempts = 3, backoff = @Backoff(delay = 5000L, multiplier = 2))
    abstract public void onMessage(T message);
    
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午2:37:35
	 */
    @Recover
    public void recover(Exception ex, Object arg0) {
    	//TODO ma wei long 后续可以考虑持久化&报警
    	log.error("AbstractMqListener - recover - arg0:{} ex",JSON.toJSONString(arg0),ex);
    }
}
