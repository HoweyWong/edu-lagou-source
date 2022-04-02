package com.lagou.edu.pay.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.dto.PayOrderDTO;
import com.lagou.edu.pay.api.dto.PayReqDTO;
import com.lagou.edu.pay.api.dto.PayResDTO;
import com.lagou.edu.pay.entity.PayOrder;
import com.lagou.edu.pay.service.IPayOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(支付控制器)   
 * @author: ma wei long
 * @date:   2020年6月17日 上午11:34:03
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

	@Autowired
    private IPayOrderService orderService;
	
	/**
	 * @Description: (查询订单信息)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 下午3:50:03   
	*/
    @GetMapping("/getOrderByNoAndUserId")
    public ResponseDTO<PayOrderDTO> getOrderByNo(@RequestParam("userId") Integer userId,@RequestParam("orderNo") String orderNo) {
    	PayOrder orderDB = orderService.getOne(new QueryWrapper<PayOrder>().eq("order_no", orderNo).eq("user_id", userId));
    	if(null == orderDB) {
    		return ResponseDTO.ofError("订单信息查询为空");
    	}
    	return ResponseDTO.success(ConvertUtils.convert(orderDB, PayOrderDTO.class));
    }
    
    /**
     * @Description: (创建订单)   
     * @author: ma wei long
     * @date:   2020年6月17日 下午3:33:18   
    */
    @PostMapping("/saveOrder")
    public ResponseDTO<PayResDTO> saveOrder(@RequestBody PayReqDTO reqDTO) {
    	log.info("saveOrder - reqDTO:{}",JSON.toJSONString(reqDTO));
        return ResponseDTO.success(orderService.saveOrder(reqDTO));
    }
    
    /**
     * @Description: (支付回调)   
     * @author: ma wei long
     * @date:   2020年6月21日 上午10:50:13   
    */
    @PostMapping("/payCallBack")
    public ResponseDTO<CallBackRes> payCallBack(@RequestBody CallBackReq request){
    	log.info("callBack - request:{}",JSON.toJSONString(request));
    	return ResponseDTO.success(orderService.callBack(request));
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年7月28日 上午11:14:01   
    */
    @GetMapping("/test")
    public ResponseDTO<PayOrderDTO> test() {
    	orderService.testTX();
    	return ResponseDTO.success();
    }
}
