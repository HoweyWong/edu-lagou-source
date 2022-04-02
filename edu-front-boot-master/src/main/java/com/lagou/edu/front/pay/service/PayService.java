package com.lagou.edu.front.pay.service;

import com.lagou.edu.front.pay.vo.request.GetPayInfoVo;
import com.lagou.edu.front.pay.vo.request.PayReqVo;
import com.lagou.edu.front.pay.vo.response.OrderVo;
import com.lagou.edu.front.pay.vo.response.PayInfoVo;
import com.lagou.edu.front.pay.vo.response.PayResVo;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;

/**
 * @Description:(支付service)   
 * @author: ma wei long
 * @date:   2020年6月17日 下午5:02:42
*/
public interface PayService {
	
	/**
	 * @Description: (创建订单(发起支付))   
	 * @author: ma wei long
	 * @date:   2020年6月17日 下午5:05:17   
	*/
	PayResVo saveOrder(PayReqVo reqVo);
	
	/**
	 * @Description: (支付回调)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 上午10:03:52   
	*/
    CallBackRes callBack(CallBackReq request);
    
    /**
     * @Description: (查询支付结果)   
     * @author: ma wei long
     * @date:   2020年6月21日 下午3:32:45   
    */
    OrderVo getPayResult(Integer userId,String orderNo);
    
    /**
     * @Description: (获取支付信息)   
     * @author: ma wei long
     * @date:   2020年6月24日 下午2:33:38   
    */
    PayInfoVo getPayInfo(GetPayInfoVo getPayInfoVo);
}
