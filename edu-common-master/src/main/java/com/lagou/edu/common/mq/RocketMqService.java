package com.lagou.edu.common.mq;

import com.lagou.edu.common.mq.dto.BaseMqDTO;

/**
 * @author: ma wei long
 * @date:   2020年6月27日 下午12:02:28
*/
public interface RocketMqService {

	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午12:02:34   
	*/
	void convertAndSend(String topic,BaseMqDTO<?> data);
	
	/**
	 * @author: ma wei long
	 * topic 可以参考MQConstant
	 * delayLevel 0 不延时   可以参考 MQConstant.DelayLevel的值
	 * @date:   2020年6月27日 下午12:40:51   
	*/
	void sendDelayed(String topic,BaseMqDTO<?> data,int delayLevel);
}
