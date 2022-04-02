package com.lagou.edu.message.api.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {

	/**
	 */
	private static final long serialVersionUID = -6579929236881323376L;
	private String type = "1";//1代表课时上架消息通知类型 暂时写死 后续类型多可以扩展枚举
	private String content;
    private Integer userId;
}
