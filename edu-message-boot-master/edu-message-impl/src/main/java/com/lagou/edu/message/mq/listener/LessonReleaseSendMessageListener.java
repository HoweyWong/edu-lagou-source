package com.lagou.edu.message.mq.listener;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.mq.listener.AbstractMqListener;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.message.api.dto.Message;
import com.lagou.edu.message.service.IMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE,
    consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE)
public class LessonReleaseSendMessageListener extends AbstractMqListener<BaseMqDTO<List<Integer>>>
    implements RocketMQListener<BaseMqDTO<List<Integer>>> {

    @Autowired
    private IMessageService messageService;

    @Override
    public void onMessage(BaseMqDTO<List<Integer>> data) {
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getMessageId());
        if (this.checkMessageId(data.getMessageId())) {
            return;
        }
        List<Integer> userIdList = data.getData();
        if (CollectionUtils.isEmpty(userIdList)) {
            return;
        }
        Message message = null;
        for (Integer userId : userIdList) {
            message = new Message();
            message.setUserId(userId);
            messageService.sendMessage(message);
        }
        this.updateMessageId(data.getMessageId());
    }
}