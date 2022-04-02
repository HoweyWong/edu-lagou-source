package com.lagou.edu.pay.trade;

import com.lagou.edu.pay.trade.request.BasePayRequest;
import com.lagou.edu.pay.trade.response.BasePayResponse;

/**
 * @Description:(三方服务抽象服务类)   
 * @author: ma wei long
 * @date:   2020年6月19日 下午1:37:45
*/
public abstract class AbstractThirdPayServer<R extends BasePayRequest,P extends BasePayResponse> implements ThirdPayServer<R,P>{

	public final static String PAY_SERVER = "_pay_server";
}
