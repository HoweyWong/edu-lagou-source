package com.lagou.edu.pay.trade;

import com.lagou.edu.pay.trade.request.BasePayRequest;
import com.lagou.edu.pay.trade.response.BasePayResponse;

/**
 * @Description:(三方支付service)   
 * @author: ma wei long
 * @date:   2020年6月19日 下午2:07:05
*/
public interface ThirdPayService {
	
	BasePayResponse submitPay(BasePayRequest request);
	
	BasePayResponse callBack(BasePayRequest request);
}
