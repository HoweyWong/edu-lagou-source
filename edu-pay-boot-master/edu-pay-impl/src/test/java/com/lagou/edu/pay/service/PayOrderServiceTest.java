package com.lagou.edu.pay.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.pay.config.AliPayMchConfig;
import com.lagou.edu.pay.config.WechatPayMchConfig;
import com.lagou.edu.pay.entity.PayOrder;
import com.lagou.edu.pay.entity.PayOrderRecord;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 上午11:40:59
*/
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PayOrderServiceTest {

    @Autowired
    private IPayOrderRecordService payOrderRecordService;
    @Autowired
    private IPayOrderService payOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
	private IPayOrderService orderService;
    
    @Value("${rocketmq.producer.group}")
    private String group;
    
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    
    @Autowired
	private WechatPayMchConfig wechatPayMchConfig;
    @Autowired
	private AliPayMchConfig aliPayMchConfig;
    
    @Test
    public void testSave() {
    	
    	PayOrder updatePayOrder = new PayOrder();
		updatePayOrder.setId(1283736323609714689L);
		updatePayOrder.setStatus(2);
		updatePayOrder.setPayTime(new Date());
		ValidateUtils.isTrue(payOrderService.updatePayOrderInfo(updatePayOrder), "支付订单更新失败");
		
//    	payOrderService.testTX();
    	System.out.println("sss");
    	redisTemplate.opsForValue().set("test", "test123");
//    	System.out.println("");
//    	Object cache = redisTemplate.opsForValue().get("test");
    	
//    	{pay_url=https://qr.alipay.com/bax06214kbynopi0pvfe204f}
    	Map<String,String> param = new HashMap<String,String>();
    	param.put("pay_url", "https://qr.alipay.com/bax06214kbynopi0pvfe204f");
    	PayOrder updateOrder = new PayOrder();
        updateOrder.setId(8479L);
        Boolean ressss = orderService.updateStatusInvalid(updateOrder);
        System.out.println("");
    	
    	
    	String aa = "{\"amount\":8000,\"channel\":\"1\",\"count\":1,\"currency\":\"GBEANS\",\"goodsOrderNo\":\"362464763119046656\",\"orderNo\":\"362658344215678976\",\"orderType\":1,\"productId\":1,\"productName\":\"《32个Java面试必考点》\",\"source\":3,\"status\":1}";
    	PayOrder order = JSON.parseObject(aa, PayOrder.class);
    	order.setCreatedTime(new Date());
        order.setUpdatedTime(order.getCreatedTime());
        order.setUserId(1);
        order.setClientIp("111");
    	boolean res11 = payOrderService.save(order);
    	System.out.println("");
    	PayOrderRecord savePayOrderRecord = new PayOrderRecord();
//    	savePayOrderRecord.setAmount(100);
    	savePayOrderRecord.setCreatedAt(new Date());
    	savePayOrderRecord.setCreatedBy("test");
//    	savePayOrderRecord.setFromStatus("C");
    	savePayOrderRecord.setOrderNo("test");
//    	savePayOrderRecord.setPaidAmount(200);
//    	savePayOrderRecord.setRemark("testtest");
    	savePayOrderRecord.setToStatus("D");
    	savePayOrderRecord.setType("1");
    	boolean res = payOrderRecordService.save(savePayOrderRecord);
    	log.info("testSave - res:{}",res);
    }
    
    @Test
    public void queryOrder() {
    	PayOrder payOrderDB = payOrderService.getOne(new QueryWrapper<PayOrder>().eq("order_no", "1"));
    	log.info("queryOrder - payOrderDB:{}",JSON.toJSONString(payOrderDB));
		ValidateUtils.isTrue(null != payOrderDB, "支付订单信息查询为空");
    }
}
