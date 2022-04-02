package com.lagou.edu.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderResDTO;
import com.lagou.edu.order.service.IUserCourseOrderService;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(支付控制器)   
 * @author: ma wei long
 * @date:   2020年6月17日 上午11:34:03
 */
@Slf4j
@RestController
@RequestMapping("/userCourseOrder")
public class UserCourseOrderController {

	@Autowired
    private IUserCourseOrderService userCourseOrderService;
	
    /**
     * @Description: (创建订单)   
     * @author: ma wei long
     * @date:   2020年6月17日 下午3:33:18   
    */
    @PostMapping("/saveOrder")
    public ResponseDTO<UserCourseOrderResDTO> saveOrder(@RequestBody CreateShopGoodsOrderReqDTO reqDTO) {
    	log.info("saveOrder - reqDTO:{}",JSON.toJSONString(reqDTO));
        return ResponseDTO.success(userCourseOrderService.saveOrder(reqDTO));
    }
    
    /**
	 * @Description: (根据订单号获取订单信息)   
	 * @author: ma wei long
	 * @date:   2020年6月19日 上午11:31:59   
	*/
    @GetMapping("/getCourseOrderByOrderNo")
    public ResponseDTO<UserCourseOrderDTO> getCourseOrderByOrderNo(@RequestParam("orderNo")String orderNo) {
    	log.info("getCourseOrderByOrderNo - orderNo:{}",orderNo);
        return ResponseDTO.success(userCourseOrderService.getCourseOrderByOrderNo(orderNo));
    }
    
    /**
	 * @Description: (更新商品订单状态)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 下午11:15:18   
	*/
    @PostMapping("/updateOrderStatus")
    public ResponseDTO<?> updateOrderStatus(@RequestParam("orderNo")String orderNo,@RequestParam("status")Integer status) {
    	log.info("updateOrderStatus - orderNo:{} status:{}",orderNo,status);
    	userCourseOrderService.updateOrderStatus(orderNo,status);
        return ResponseDTO.success();
    }
    
    /**
	 * @Description: (根据用户id查询商品订单)   
	 * @author: ma wei long
	 * @date:   2020年6月22日 下午8:22:43   
	*/
	@GetMapping("/getUserCourseOrderByUserId")
	ResponseDTO<List<UserCourseOrderDTO>> getUserCourseOrderByUserId(@RequestParam("userId") Integer userId){
		log.info("getUserCourseOrderByUserId - userId:{}",userId);
		return ResponseDTO.success(userCourseOrderService.getUserCourseOrderByUserId(userId));
	}
	
	/**
	 * @Description: (根据用户&课程id统计订单数量)   
	 * @author: ma wei long
	 * @date:   2020年6月22日 下午8:22:43   
	*/
	@GetMapping("/countUserCourseOrderByCoursIds")
	ResponseDTO<Integer> countUserCourseOrderByCoursIds(@RequestParam("userId") Integer userId,@RequestParam("coursIds") List<Integer> coursIds){
		log.info("countUserCourseOrderByCoursIds - userId:{} coursIds:{}",userId,JSON.toJSONString(coursIds));
		return ResponseDTO.success(userCourseOrderService.countUserCourseOrderByCoursIds(userId, coursIds));
	}
	
	/**
	 * @Description: (根据课程id统计支付成功订单数量)   
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:28:05   
	*/
	@GetMapping("/countUserCourseOrderByCourseId")
	ResponseDTO<Integer> countUserCourseOrderByCourseId(@RequestParam("coursId") Integer coursId){
		log.info("countUserCourseOrderByCourseId - coursId:{} ",coursId);
		return ResponseDTO.success(userCourseOrderService.countUserCourseOrderByCourseId(coursId));
	}
	
	/**
	 * @Description: (根据课程id查询支付成功订单集合)   
	 * @author: ma wei long
	 * @date:   2020年6月30日 上午10:40:47   
	*/
	@GetMapping("/getOrderListByCourseId")
	ResponseDTO<List<UserCourseOrderDTO>> getOrderListByCourseId(@RequestParam("coursId") Integer coursId){
		log.info("getOrderListByCourseId - coursId:{} ",coursId);
		return ResponseDTO.success(userCourseOrderService.getOrderListByCourseId(coursId));
	}
}
