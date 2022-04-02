package com.lagou.edu.front.order.vo.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:(创建商城商品订单的请求)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午7:35:42
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "创建商品订单请求")
public class CreateShopGoodsOrderReqVo implements java.io.Serializable{
    /**
	*/
	private static final long serialVersionUID = 2463894259231186820L;
	
	@ApiModelProperty(value = "在售商品ID" , required = true, dataType = "integer")
    private Integer goodsId;
	
	private Integer userId;
}
