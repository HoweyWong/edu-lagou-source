package com.lagou.edu.course.mq.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.mq.listener.AbstractMqListener;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.dto.ActivityCourseUpdateStockDTO;
import com.lagou.edu.course.entity.po.ActivityCourse;
import com.lagou.edu.course.mapper.ActivityCourseMapper;
import com.lagou.edu.course.service.IActivityCourseService;

import lombok.extern.slf4j.Slf4j;
/**
 * @author: ma wei long
 * @date:   2020年7月8日 上午11:06:35
 */
@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.ACTIVITY_COURSE_STOCK, consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.ACTIVITY_COURSE_STOCK)
public class ActitvityCourseStockListener extends AbstractMqListener<BaseMqDTO<ActivityCourseUpdateStockDTO>> implements RocketMQListener<BaseMqDTO<ActivityCourseUpdateStockDTO>>{
	
	@Autowired
	private IActivityCourseService activityCourseService;
	@Autowired
	private ActivityCourseMapper activityCourseMapper;
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月30日 上午10:21:50
	*/
    @Override
    public void onMessage(BaseMqDTO<ActivityCourseUpdateStockDTO> data) {
    	log.info("onMessage - data:{}",JSON.toJSONString(data));
    	ValidateUtils.notNullParam(data);
    	ValidateUtils.notNullParam(data.getMessageId());
    	
    	if(this.checkMessageId(data.getMessageId())) {
    		return;
    	}
    	ActivityCourse activityCourseDB = activityCourseService.getById(data.getData().getId());
    	if(null == activityCourseDB) {
    		this.updateMessageId(data.getMessageId());
    		return;
    	}
    	int res = activityCourseMapper.updateStock(data.getData().getId(),1);
    	log.info("onMessage - activityCourseMapper.updateStock id:{} res:{}",data.getData().getId(),res);
    	ValidateUtils.isTrue(res == 1, "updateStock is fail id:" + data.getData().getId());
    	this.updateMessageId(data.getMessageId());
    }
}