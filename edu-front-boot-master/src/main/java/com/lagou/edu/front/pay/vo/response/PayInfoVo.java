package com.lagou.edu.front.pay.vo.response;

import java.math.BigDecimal;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月24日 下午2:32:09
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayInfoVo {

	@ApiModelProperty(value = "价格, 单位元")
    private BigDecimal price;
	
    @ApiModelProperty(value = "当前订单支持的支付渠道信息")
    private List<OrderSupportChannel> supportChannels;
}
