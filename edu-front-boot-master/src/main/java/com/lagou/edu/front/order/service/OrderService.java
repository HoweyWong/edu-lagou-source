package com.lagou.edu.front.order.service;

import com.lagou.edu.front.order.vo.request.CreateShopGoodsOrderReqVo;
import com.lagou.edu.front.order.vo.response.CreateShopGoodsOrderResVo;

/**
 * @Description:(商品订单service)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午7:40:11
 */
public interface OrderService {
	
	/**
	 * @Description: (创建商品订单)   
	 * @author: ma wei long
	 * @date:   2020年6月18日 下午7:40:30   
	*/
	CreateShopGoodsOrderResVo saveOrder(CreateShopGoodsOrderReqVo reqVo);
}
