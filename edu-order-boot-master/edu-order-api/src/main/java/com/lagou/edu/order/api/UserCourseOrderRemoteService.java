package com.lagou.edu.order.api;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.order.api.dto.CreateShopGoodsOrderReqDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.dto.UserCourseOrderResDTO;

/**
 * @Description:(订单服务)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午4:34:13
 */
@FeignClient(name = "${remote.feign.edu-order-boot.name:edu-order-boot}", path = "/userCourseOrder")
public interface UserCourseOrderRemoteService {

	/**
	 * @Description: (保存支付订单)   
	 * @author: ma wei long
	 * @date:   2020年6月19日 上午11:31:37   
	*/
	@PostMapping("/saveOrder")
	ResponseDTO<UserCourseOrderResDTO> saveOrder(@RequestBody CreateShopGoodsOrderReqDTO reqDTO);
	
	/**
	 * @Description: (根据订单号获取订单信息)   
	 * @author: ma wei long
	 * @date:   2020年6月19日 上午11:31:59   
	*/
	@GetMapping("/getCourseOrderByOrderNo")
	ResponseDTO<UserCourseOrderDTO> getCourseOrderByOrderNo(@RequestParam("orderNo") String orderNo);
	
	/**
	 * @Description: (更新商品订单状态)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 下午11:15:18   
	*/
	@PostMapping("/updateOrderStatus")
	ResponseDTO<?> updateOrderStatus(@RequestParam("orderNo")String orderNo,@RequestParam("status")Integer status);
	
	/**
	 * @Description: (根据用户id查询商品订单)   
	 * @author: ma wei long
	 * @date:   2020年6月22日 下午8:22:43   
	*/
	@GetMapping("/getUserCourseOrderByUserId")
	ResponseDTO<List<UserCourseOrderDTO>> getUserCourseOrderByUserId(@RequestParam("userId") Integer userId);
	
	/**
	 * @Description: (根据用户&课程id统计订单数量)   
	 * @author: ma wei long
	 * @date:   2020年6月23日 上午11:58:29   
	*/
	@GetMapping("/countUserCourseOrderByCoursIds")
	ResponseDTO<Integer> countUserCourseOrderByCoursIds(@RequestParam("userId") Integer userId,@RequestParam("coursIds") List<Integer> coursIds);

	/**
	 * @Description: (根据课程id统计支付成功订单数量)   
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:28:05   
	*/
	@GetMapping("/countUserCourseOrderByCourseId")
	ResponseDTO<Integer> countUserCourseOrderByCourseId(@RequestParam("coursId") Integer coursId);
	
	/**
	 * @Description: (根据课程id查询支付成功订单集合)   
	 * @author: ma wei long
	 * @date:   2020年6月30日 上午10:40:47   
	*/
	@GetMapping("/getOrderListByCourseId")
	ResponseDTO<List<UserCourseOrderDTO>> getOrderListByCourseId(@RequestParam("coursId") Integer coursId);
}
