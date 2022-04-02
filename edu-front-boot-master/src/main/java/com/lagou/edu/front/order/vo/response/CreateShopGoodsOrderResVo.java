package com.lagou.edu.front.order.vo.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:(创建订单返回结果)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午7:37:11
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "创建商品订单的返回结果")
public class CreateShopGoodsOrderResVo implements java.io.Serializable{

	/**
	 */
	private static final long serialVersionUID = 9166826521526543552L;
	@ApiModelProperty(value = "商品订单号")
	private String orderNo;//订单号
}
