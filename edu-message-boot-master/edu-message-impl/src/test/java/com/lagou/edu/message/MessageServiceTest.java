package com.lagou.edu.message;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.h2.engine.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lagou.edu.message.entity.Message;
import com.lagou.edu.message.service.IMessageService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 上午11:40:59
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
	private IMessageService messageService;

    
    @Test
    public void test() {
//    	for(int i = 0; i < 50; i++) {
//    		Message message = new Message();
//    		message.setCourseId(i);
//    		message.setCourseLessonId(i);
//    		message.setCourseName("testCourseName" + i);
//    		message.setCreateTime(new Date());
//    		message.setTheme("testTheme" + i);
//    		message.setUpdateTime(new Date());
//    		message.setUserId(i);
//    		messageService.save(message);
//    	}
    	System.out.println("");
    	QueryWrapper<Message> wrapper = new QueryWrapper();
//        wrapper.like("name", "雨").lt("age", 40);
 
    	IPage<Message> page = new Page<Message>(1,30);
        IPage<Message> mapIPage = messageService.page(page, wrapper);
 
        System.out.println("总页数"+mapIPage.getPages());
        System.out.println("总记录数"+mapIPage.getTotal());
        List<Message> records = mapIPage.getRecords();
        records.forEach(System.out::println);
    }
}
