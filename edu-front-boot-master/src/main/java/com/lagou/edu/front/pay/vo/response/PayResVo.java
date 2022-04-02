package com.lagou.edu.front.pay.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:(支付相应对象)   
 * @author: ma wei long
 * @date:   2020年6月17日 下午4:34:06
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayResVo implements Serializable{

    /**
	 */
	private static final long serialVersionUID = 582033193186893896L;
	
	@ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "渠道 weChat-微信支付，aliPay-支付宝支付")
    private String channel;
    @ApiModelProperty(value = "来源 1-app 2-h5 3-pc 4-jsapi 5-app-ios 6-app-android")
    private Integer source;
    @ApiModelProperty(value = "支付所需字符串")
    private String payUrl;
    @ApiModelProperty(value = "订单状态：1-未支付 2-支付成功 3-支付失败")
    private Integer status;
}
