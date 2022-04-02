package com.lagou.edu.front.pay.vo.request;

import java.io.Serializable;

import com.lagou.edu.pay.api.enums.Source;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:支付请求对象   
 * @author: ma wei long
 * @date:   2020年6月17日 下午3:30:52
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayReqVo implements Serializable{

    /**
	 */
	private static final long serialVersionUID = 3970659330212218613L;
	
	@ApiModelProperty(value = "商品订单编号")
    private String goodsOrderNo;
    @ApiModelProperty(value = "渠道(weChat-微信支付，aliPay-支付宝支付)", required = true)
    private String channel;
    @ApiModelProperty(value = "h5支付成功回调地址")
    private String returnUrl;
    @ApiModelProperty(value = "如果是从微信浏览器过来的话，则使用该参数来获取真正的openId参数")
    private Integer wxType;
    private Source source = Source.PC;// 支付来源
    private Integer userid;//用户ID
    private String clientIp;// 客户端IP地址
}
