package com.lagou.edu.common.mq.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月27日 上午11:59:54
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseMqDTO<T> implements Serializable{

	/**
	 */
	private static final long serialVersionUID = -1762409052644257813L;
	
	private T data;//消息体数据
	
	private String messageId;
}
