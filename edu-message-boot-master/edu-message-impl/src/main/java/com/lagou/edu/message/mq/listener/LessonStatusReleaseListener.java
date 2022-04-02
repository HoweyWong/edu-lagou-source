package com.lagou.edu.message.mq.listener;

import java.util.List;
import java.util.UUID;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.mq.RocketMqService;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.mq.listener.AbstractMqListener;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.message.api.dto.LessonStatusReleaseDTO;
import com.lagou.edu.message.service.IMessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RocketMQMessageListener(topic = MQConstant.Topic.LESSON_STATUS_RELEASE,
    consumerGroup = "${rocketmq.producer.group}" + "_" + MQConstant.Topic.LESSON_STATUS_RELEASE)
public class LessonStatusReleaseListener extends AbstractMqListener<BaseMqDTO<LessonStatusReleaseDTO>>
    implements RocketMQListener<BaseMqDTO<LessonStatusReleaseDTO>> {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private RocketMqService rocketMqService;

    @Override
    public void onMessage(BaseMqDTO<LessonStatusReleaseDTO> data) {
        ValidateUtils.notNullParam(data);
        ValidateUtils.notNullParam(data.getMessageId());
        if (this.checkMessageId(data.getMessageId())) {
            return;
        }
        List<Integer> userIdList = messageService.saveMessage(data.getData().getLessonId());
        if (!CollectionUtils.isEmpty(userIdList)) {
            rocketMqService.convertAndSend(MQConstant.Topic.LESSON_RELEASE_SEND_MESSAGE,
                new BaseMqDTO<>(userIdList, UUID.randomUUID().toString()));
        }
        this.updateMessageId(data.getMessageId());
    }
}