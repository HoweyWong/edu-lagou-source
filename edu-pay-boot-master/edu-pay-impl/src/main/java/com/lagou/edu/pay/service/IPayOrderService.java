package com.lagou.edu.pay.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.dto.PayReqDTO;
import com.lagou.edu.pay.api.dto.PayResDTO;
import com.lagou.edu.pay.entity.PayOrder;
import com.lagou.edu.pay.mq.dto.CancelPayOrderDTO;
/**
 * @author: ma wei long
 * @date:   2020年6月17日 上午11:43:48
 */
public interface IPayOrderService extends IService<PayOrder>{

	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    PayResDTO saveOrder(PayReqDTO reqDTO);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    CallBackRes callBack(CallBackReq request);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    PayOrder getLastOrder(Integer userId,Integer courseId,String channel);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    boolean updatePayOrderInfo(PayOrder order);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    PayOrder saveOrder(PayReqDTO reqDTO,CourseDTO courseDTO,BigDecimal activityAmount);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    void cancelPayOrder(CancelPayOrderDTO cancelPayOrderDTO);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    boolean updateStatusInvalid(PayOrder payOrder);
    
    /**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
    void testTX();
}
