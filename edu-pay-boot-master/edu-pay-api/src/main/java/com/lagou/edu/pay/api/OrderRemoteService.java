package com.lagou.edu.pay.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.dto.PayOrderDTO;
import com.lagou.edu.pay.api.dto.PayReqDTO;
import com.lagou.edu.pay.api.dto.PayResDTO;

/**
 * ma wei long
 * 2020年6月17日 上午11:40:59
 */
@FeignClient(name = "${remote.feign.edu-pay-boot.name:edu-pay-boot}", path = "/order")
public interface OrderRemoteService {

	/**
	 * @Description: (查询订单信息)   
	 * @author: ma wei long
	 * @date:   2020年6月22日 下午3:26:58   
	*/
    @GetMapping("/getOrderByNoAndUserId")
    ResponseDTO<PayOrderDTO> getOrderByNoAndUserId(@RequestParam("userId") Integer userId,@RequestParam("orderNo") String orderNo);
    
    /**
     * @Description: (保存订单信息)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午3:27:35   
    */
    @PostMapping("/saveOrder")
    ResponseDTO<PayResDTO> saveOrder(@RequestBody PayReqDTO reqDTO);
    
    /**
     * @Description: (支付结果回调通知)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午3:28:00   
    */
    @PostMapping("/payCallBack")
    ResponseDTO<CallBackRes> payCallBack(@RequestBody CallBackReq request);
}