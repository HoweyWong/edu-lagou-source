package com.lagou.edu.pay.trade.response;

import java.io.Serializable;

import com.lagou.edu.pay.model.OutTrade;

import lombok.Data;

/**
 * @Description:(支付基础响应对象)   
 * @author: ma wei long
 * @date:   2020年6月19日 下午1:47:34
*/
@Data
public class BasePayResponse implements Serializable{

	/**
	 */
	private static final long serialVersionUID = 942497671183248456L;
	public boolean isSuccess() {return true;};
	private String url;
	private OutTrade trade;
	private String resStr;
}
