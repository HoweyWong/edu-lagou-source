package com.lagou.edu.pay.trade;

import com.lagou.edu.pay.trade.request.BasePayRequest;
import com.lagou.edu.pay.trade.response.BasePayResponse;

/**
 * @Description:(三方支付接口服务)   
 * @author: ma wei long
 * @date:   2020年6月19日 下午1:35:29
*/
public interface ThirdPayServer<R extends BasePayRequest,P extends BasePayResponse> {

	/**
	 * @Description: (提交支付请求 )   
	 * @author: ma wei long
	 * @date:   2020年6月19日 下午1:36:08   
	*/
	P submitPay(R request);
	
	/**
	 * @Description: (支付回调)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 上午10:56:28   
	*/
	P callBack(R request);
}
