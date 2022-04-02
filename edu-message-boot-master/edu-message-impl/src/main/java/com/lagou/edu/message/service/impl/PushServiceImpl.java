package com.lagou.edu.message.service.impl;
import org.springframework.stereotype.Service;

import com.lagou.edu.message.api.dto.Message;
import com.lagou.edu.message.service.IPushService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PushServiceImpl implements IPushService {
	
	/**
     * 向页面推送消息
    */
	@Override
	public void push(Message message) {
		try {
            long l1 = System.currentTimeMillis();
//            PushServer.pushServer.push(message);
            final long l2 = System.currentTimeMillis() - l1;
            if (l2 > 50l) {
                log.info("push-impl耗时,time={}ms", l2);
            }
        } finally {
        	
        }
	}
}
