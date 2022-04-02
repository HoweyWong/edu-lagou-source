package com.lagou.edu.front.message.vo.request;

import com.lagou.edu.common.page.BasePageVO;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: ma wei long
 * @date:   2020年6月30日 下午8:37:21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "通知消息查询对象")
public class MessageQueryVo extends BasePageVO implements java.io.Serializable{
	
	/**
	 */
	private static final long serialVersionUID = 6459381920809382868L;
	
	private Integer userId;
}
