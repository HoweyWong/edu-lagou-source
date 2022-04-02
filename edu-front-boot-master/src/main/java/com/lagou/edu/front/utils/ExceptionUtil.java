package com.lagou.edu.front.utils;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.front.order.vo.request.CreateShopGoodsOrderReqVo;

import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年7月8日 下午10:02:23
*/
@Slf4j
public final class ExceptionUtil {
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月9日 下午5:52:08   
	*/
    public static ResponseDTO<String> saveOrderHandleException(CreateShopGoodsOrderReqVo reqVo, HttpServletRequest request,BlockException ex) {
    	log.error("提交课程订单 saveOrderHandleException :{}", ex.getClass().getCanonicalName(),ex);
        return ResponseDTO.ofError(ResultCode.FLOW_SENTINEL_ERROR.getState(), ResultCode.FLOW_SENTINEL_ERROR.getMessage());
    }
    
    /**
     * @author: ma wei long
     * @date:   2020年7月10日 上午10:30:12   
    */
    public static ResponseDTO<String> testHandleException(BlockException ex) {
    	log.error("testHandleException :{}", ex.getClass().getCanonicalName(),ex);
        return ResponseDTO.ofError(ResultCode.FLOW_SENTINEL_ERROR.getState(), ResultCode.FLOW_SENTINEL_ERROR.getMessage());
    }
}
