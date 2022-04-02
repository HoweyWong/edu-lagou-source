package com.lagou.edu.order.service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import com.lagou.edu.common.key.IPKeyGenerator;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.enums.UserCourseOrderSourceType;
import com.lagou.edu.order.entity.UserCourseOrder;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 上午11:40:59
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private IUserCourseOrderRecordService userCourseOrderRecordService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IUserCourseOrderService userCourseOrderService;
    @Autowired
    private KeyGenerator keyGenerator;
    
    @Test
    public void queryOrder() {
    	
    	UserCourseOrderDTO userCourseOrderDTO = userCourseOrderService.getCourseOrderByOrderNo("489391528578383873");
    	log.info("getCourseOrderByOrderNo - userCourseOrderDTO:{}",JSON.toJSONString(userCourseOrderDTO));
    
    	userCourseOrderService.updateOrderStatus("489391528578383873", 20);
    	log.info("updateOrderStatus");
    	
    	List<UserCourseOrderDTO> orderList = userCourseOrderService.getUserCourseOrderByUserId(9);
    	log.info("getUserCourseOrderByUserId - orderList:{}",JSON.toJSONString(orderList));
    	
    	Integer countOrder = userCourseOrderService.countUserCourseOrderByCoursIds(9, Arrays.asList(1));
    	log.info("countUserCourseOrderByCoursIds - countOrder:{}",countOrder);
    	
    	Integer orderRes = userCourseOrderService.countUserCourseOrderByCourseId(1);
    	log.info("countUserCourseOrderByCourseId - orderRes:{}",orderRes);
    	
    	List<UserCourseOrderDTO> res = userCourseOrderService.getOrderListByCourseId(1);
    	log.info("getOrderListByCourseId - res:{}",res);
    }
    public static void main(String[] args) {
		System.out.println(100030021 % 10);
	}
    
    @Test
    public void saveOrder() {
    	List<UserCourseOrderDTO> aa = userCourseOrderService.getUserCourseOrderByUserId(100030021);
    	for(int i = 0; i < 10; i++) {
    		UserCourseOrder userCourseOrder = new UserCourseOrder();
        	userCourseOrder.setCourseId(1);
        	userCourseOrder.setCreateTime(new Date());
        	userCourseOrder.setId(Long.parseLong(keyGenerator.generateKey().toString()));
        	userCourseOrder.setOrderNo(keyGenerator.generateKey().toString());
        	userCourseOrder.setSourceType(UserCourseOrderSourceType.USER_BUY.getCode());
        	userCourseOrder.setUpdateTime(new Date());
        	userCourseOrder.setUserId(100030021);
        	userCourseOrderService.save(userCourseOrder);
        	System.out.println("");
    	}
    }
    @Test
    public void testSave() {
    	Integer aa = userCourseOrderService.countUserCourseOrderByCourseId(153);
    	System.out.println("");
//    	System.out.println("sss");
//    	redisTemplate.opsForValue().set("test", "test");
//    	
//    	System.out.println("");
//    	Object cache = redisTemplate.opsForValue().get("test");
//    	
//    	UserCourseOrderRecord saveUserCourseOrderRecord = new UserCourseOrderRecord();
//    	saveUserCourseOrderRecord.setCreateTime(new Date());
//    	saveUserCourseOrderRecord.setCreateUser("test");
//    	saveUserCourseOrderRecord.setFromStatus("a");
//    	saveUserCourseOrderRecord.setOrderNo("13");
//    	saveUserCourseOrderRecord.setRemark("tetest");
//    	saveUserCourseOrderRecord.setToStatus("b");
//    	saveUserCourseOrderRecord.setUpdateTime(saveUserCourseOrderRecord.getCreateTime());
//    	saveUserCourseOrderRecord.setUpdateUser("test");
//    	boolean res = userCourseOrderRecordService.save(saveUserCourseOrderRecord);
//    	log.info("testSave - res:{}",res);
//    	List<Integer> coursIds = new ArrayList<Integer>();
//    	coursIds.add(1);
//    	Integer aa = userCourseOrderService.countUserCourseOrderByCoursIds(1, coursIds);
//    	
//    	coursIds.add(2);
//    	aa = userCourseOrderService.countUserCourseOrderByCoursIds(1, coursIds);
//    	System.out.println("sss");
    	
    	List<UserCourseOrderDTO> aad = userCourseOrderService.getUserCourseOrderByUserId(1);
    	System.out.println("sss");
    }
}
