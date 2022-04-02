package com.lagou.edu.front.pay.vo.request;

import java.io.Serializable;

import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月24日 下午2:30:29
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetPayInfoVo implements Serializable{

    /**
	 */
	private static final long serialVersionUID = -2334521227503191272L;
	
	@ApiParam(value = "商品订单编号")
    private String shopOrderNo;
}
