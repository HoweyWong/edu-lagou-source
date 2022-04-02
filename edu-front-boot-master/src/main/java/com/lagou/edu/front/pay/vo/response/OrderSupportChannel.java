package com.lagou.edu.front.pay.vo.response;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author MrPro
 * @description: 订单中所支持的渠道信息
 * @date 2020-02-19 20:37:00
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "当前订单支持的渠道信息")
public class OrderSupportChannel implements Serializable {

    /**
	 */
	private static final long serialVersionUID = 5444342235204061319L;

	/**
     * 渠道码{@link com.lagou.plat.pay.enums.Channel}
     */
    @ApiModelProperty(value = "渠道码: 1:微信, 2:支付宝支付")
    private Integer channelCode;
}
