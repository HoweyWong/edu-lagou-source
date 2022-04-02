package com.lagou.edu.pay.api.dto;

import java.io.Serializable;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月22日 下午4:14:32
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CallBackReq implements Serializable {

    /**
	 */
	private static final long serialVersionUID = 8689003597912687786L;
	
	private String channel;//支付渠道
	
	private String wxCallBackReqStr;//微信回调请求参数
	
	private Map<String, String> aliParams;//支付宝回调请求参数
}
