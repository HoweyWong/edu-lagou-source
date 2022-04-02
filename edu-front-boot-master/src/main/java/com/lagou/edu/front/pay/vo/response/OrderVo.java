package com.lagou.edu.front.pay.vo.response;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:(订单信息)
 * @author: ma wei long
 * @date:   2020年6月17日 下午3:40:26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderVo implements Serializable{

    /**
	 */
	private static final long serialVersionUID = 6976336125117273264L;
	
	@ApiModelProperty(value = "订单号")
    private String orderNo;
    @ApiModelProperty(value = "商品唯一标识")
    private Integer productId;
    @ApiModelProperty(value = "付款金额，单位为分")
    private Double amount;
    @ApiModelProperty(value = "货币类型，cny-人民币 gbeans-勾豆")
    private String currency;
    @ApiModelProperty(value = "支付渠道：weChat-微信支付，aliPay-支付宝支付,applePay-苹果支付")
    private String channel;
    @ApiModelProperty(value = "订单状态：1-未支付 2-支付成功 3-支付失败")
    private Integer status;
    @ApiModelProperty(value = "类型 1-购买课程 2-充值")
    private Integer orderType;
    @ApiModelProperty(value = "支付来源 1-app 2-h5 3-pc")
    private Integer source;
    @ApiModelProperty(value = "支付时间")
    private Date payTime;
}
