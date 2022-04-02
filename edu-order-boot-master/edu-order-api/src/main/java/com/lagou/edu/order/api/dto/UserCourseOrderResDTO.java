package com.lagou.edu.order.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * @Description:(商品订单保存返回对象)   
 * @author: ma wei long
 * @date:   2020年6月18日 下午5:21:19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseOrderResDTO implements Serializable{
	
	/**
	 */
	private static final long serialVersionUID = 8581183520872075235L;
	
	private String orderNo;//订单号

}