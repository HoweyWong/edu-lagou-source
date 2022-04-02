package com.lagou.edu.pay.mq.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.mq.listener.AbstractMqListener;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.pay.mq.dto.CancelPayOrderDTO;
import com.lagou.edu.pay.service.IPayOrderService;

import lombok.extern.slf4j.Slf4j;
/**
 * @author: ma wei long
 * @date:   2020年6月27日 下午2:35:42
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.CANCEL_PAY_ORDER, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.CANCEL_PAY_ORDER)
public class CancelPayOrderListener extends AbstractMqListener<BaseMqDTO<CancelPayOrderDTO>> implements RocketMQListener<BaseMqDTO<CancelPayOrderDTO>>{
	
	@Autowired
	private IPayOrderService payOrderService;
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月27日 下午2:35:42
	 */
    @Override
    public void onMessage(BaseMqDTO<CancelPayOrderDTO> data) {
    	log.info("onMessage - data:{}",JSON.toJSONString(data));
    	ValidateUtils.notNullParam(data);
    	ValidateUtils.notNullParam(data.getMessageId());
    	
    	if(this.checkMessageId(data.getMessageId())) {
    		return;
    	}
    	
		CancelPayOrderDTO cancelPayOrderDTO = data.getData();
    	ValidateUtils.notNullParam(cancelPayOrderDTO);
    	
    	Long orderId = cancelPayOrderDTO.getOrderId();
    	ValidateUtils.notNullParam(orderId);
    	
    	payOrderService.cancelPayOrder(cancelPayOrderDTO);
    	this.updateMessageId(data.getMessageId());
    }
}