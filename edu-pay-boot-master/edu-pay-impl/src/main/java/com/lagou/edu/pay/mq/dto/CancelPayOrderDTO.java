package com.lagou.edu.pay.mq.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月27日 下午2:27:44
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CancelPayOrderDTO implements Serializable{
	
	/**
	 */
	private static final long serialVersionUID = -191417001399470940L;
	
	private Long orderId;
}
