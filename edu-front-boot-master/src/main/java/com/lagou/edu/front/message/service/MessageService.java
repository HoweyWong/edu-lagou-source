package com.lagou.edu.front.message.service;

import com.lagou.edu.common.page.DataGrid;
import com.lagou.edu.front.message.vo.request.MessageQueryVo;
import com.lagou.edu.front.message.vo.response.MessageVo;

/**
 * @author: ma wei long
 * @date:   2020年6月28日 下午2:37:31
 */
public interface MessageService {
	
	/**
	 * @Description: (创建商品订单)   
	 * @author: ma wei long
	 * @date:   2020年6月18日 下午7:40:30   
	*/
	DataGrid<MessageVo> getMessageList(MessageQueryVo param);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年6月29日 上午11:19:55   
	*/
	Boolean updateReadStatus(Integer userId);
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月13日 下午8:00:37   
	*/
	Boolean getUnReadMessageFlag(Integer userId);
}
