package com.lagou.edu.front.pay.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.klock.annotation.Klock;
import org.springframework.boot.autoconfigure.klock.model.LockTimeoutStrategy;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.common.util.ConvertUtils;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.ActivityCourseRemoteService;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.front.pay.service.PayService;
import com.lagou.edu.front.pay.vo.request.GetPayInfoVo;
import com.lagou.edu.front.pay.vo.request.PayReqVo;
import com.lagou.edu.front.pay.vo.response.OrderSupportChannel;
import com.lagou.edu.front.pay.vo.response.OrderVo;
import com.lagou.edu.front.pay.vo.response.PayInfoVo;
import com.lagou.edu.front.pay.vo.response.PayResVo;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.enums.UserCourseOrderStatus;
import com.lagou.edu.pay.api.OrderRemoteService;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.dto.PayOrderDTO;
import com.lagou.edu.pay.api.dto.PayReqDTO;
import com.lagou.edu.pay.api.dto.PayResDTO;
import com.lagou.edu.pay.api.enums.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(支付service)
 * @author: ma wei long
 * @date: 2020年6月17日 下午5:03:19
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {

	@Autowired
	private OrderRemoteService payRemoteService;
	@Autowired
	private UserCourseOrderRemoteService userCourseOrderRemoteService;
	@Autowired
	private CourseRemoteService courseRemoteService;
	@Autowired
    private ActivityCourseRemoteService activityCourseRemoteService;

	/**
	 * @Description: (创建订单(发起支付))
	 * @author: ma wei long
	 * @date: 2020年6月17日 下午5:05:17
	 */
	@Override
	@Klock(keys = {"#reqVo.userid","#reqVo.goodsOrderNo"}, waitTime = 0 ,leaseTime = 120,lockTimeoutStrategy = LockTimeoutStrategy.FAIL_FAST)
	public PayResVo saveOrder(PayReqVo reqVo) {
		ValidateUtils.notNullParam(reqVo);
		ValidateUtils.notBlank(reqVo.getGoodsOrderNo(), "商品编号不能为空");
		ValidateUtils.notBlank(reqVo.getChannel(), "渠道不能为空");
		ValidateUtils.notBlank(reqVo.getReturnUrl(), "h5回调url不能为空");
		ResponseDTO<UserCourseOrderDTO> resp = userCourseOrderRemoteService.getCourseOrderByOrderNo(reqVo.getGoodsOrderNo());
		log.info("saveOrder - userCourseOrderRemoteService.getCourseOrderByOrderNo - goodsOrderNo:{} resp:{}",reqVo.getGoodsOrderNo(),JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		
		UserCourseOrderDTO goodsOrderInfo = resp.getContent();
		ValidateUtils.isTrue(goodsOrderInfo.getUserId().equals(reqVo.getUserid()), "商品订单用户id错误");
		ValidateUtils.isTrue(goodsOrderInfo.getStatus().equals(UserCourseOrderStatus.CREATE.getCode()), "商品订单状态错误");
		
		reqVo.setUserid(reqVo.getUserid());
		ResponseDTO<PayResDTO> payResDTOResp = payRemoteService.saveOrder(ConvertUtils.convert(reqVo, PayReqDTO.class));
		log.info("saveOrder - payRemoteService.saveOrder - reqVo:{} payResDTOResp:{}",JSON.toJSONString(reqVo),JSON.toJSONString(payResDTOResp));
		ValidateUtils.isTrue(payResDTOResp.isSuccess(), payResDTOResp.getState(),payResDTOResp.getMessage());
		
		return ConvertUtils.convert(payResDTOResp.getContent(), PayResVo.class);
	}

	/**
	 * @Description: (支付回调)   
	 * @author: ma wei long
	 * @date:   2020年6月21日 上午10:03:52   
	*/
	@Override
	public CallBackRes callBack(CallBackReq request) {
		ResponseDTO<CallBackRes> resp = payRemoteService.payCallBack(request);
		log.info("callBack - payRemoteService.payCallBack - request:{} resp:{}",JSON.toJSONString(request),JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		return resp.getContent();
	}

	/**
     * @Description: (查询支付结果)   
     * @author: ma wei long
     * @date:   2020年6月21日 下午3:32:45   
    */
	@Override
	public OrderVo getPayResult(Integer userId, String orderNo) {
		log.info("getPayResult - userId:{} orderNo:{}",userId,orderNo);
		ValidateUtils.notNullParam(userId);
		ValidateUtils.notBlank(orderNo, "订单号不能为空");
		ResponseDTO<PayOrderDTO> resp = payRemoteService.getOrderByNoAndUserId(userId, orderNo);
		log.info("getPayResult - payRemoteService.getOrderByNoAndUserId - userId:{} orderNo:{} resp:{}",userId,orderNo,JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		PayOrderDTO orderDTO = resp.getContent();
		ValidateUtils.notNull(orderDTO, ResultCode.ALERT_ERROR.getState(), "订单信息查询为空");
		return ConvertUtils.convert(orderDTO, OrderVo.class);
	}

	/**
     * @Description: (获取支付信息)   
     * @author: ma wei long
     * @date:   2020年6月24日 下午2:33:38   
    */
	@Override
	public PayInfoVo getPayInfo(GetPayInfoVo getPayInfoVo) {
		ValidateUtils.notNullParam(getPayInfoVo);
		ValidateUtils.notBlank(getPayInfoVo.getShopOrderNo(), "订单号不能为空");
		
		ResponseDTO<UserCourseOrderDTO> resp = userCourseOrderRemoteService.getCourseOrderByOrderNo(getPayInfoVo.getShopOrderNo());
		log.info("getPayInfo - userCourseOrderRemoteService.getCourseOrderByOrderNo - req:{} resp:{}",getPayInfoVo.getShopOrderNo(),JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		
		UserCourseOrderDTO userCourseOrderDTO = resp.getContent();
		ValidateUtils.notNull(userCourseOrderDTO, ResultCode.ALERT_ERROR.getState(), "课程订单信息查询为空");

		CourseDTO courseDTO = courseRemoteService.getCourseById(userCourseOrderDTO.getCourseId(),userCourseOrderDTO.getUserId());
		log.info("getPayInfo - courseRemoteService.getCourseById - courseId:{} userId:{} resp:{}",userCourseOrderDTO.getCourseId(),userCourseOrderDTO.getUserId(),JSON.toJSONString(courseDTO));
		ValidateUtils.notNull(courseDTO, ResultCode.ALERT_ERROR.getState(), "课程信息查询为空");
		
		PayInfoVo res = new PayInfoVo();
    	List<OrderSupportChannel> supportChannels = Lists.newArrayList();
    	OrderSupportChannel aliPay = new OrderSupportChannel();
    	aliPay.setChannelCode(Channel.ALIPAY.getCode());
    	OrderSupportChannel weChatPay = new OrderSupportChannel();
    	weChatPay.setChannelCode(Channel.WECHAT.getCode());
    	supportChannels.add(aliPay);
    	supportChannels.add(weChatPay);
    	
    	BigDecimal activityAmount = null;
    	if(resp.getContent().getActivityCourseId() > 0) {
    		//查询课程活动金额
    		ResponseDTO<ActivityCourseDTO> respAC = activityCourseRemoteService.getById(resp.getContent().getActivityCourseId());
    		ValidateUtils.isTrue(respAC.isSuccess(), respAC.getMessage());
    		activityAmount = new BigDecimal(respAC.getContent().getAmount());
    	}
    	if(null != activityAmount) {
    		res.setPrice(activityAmount);
        }else {
        	res.setPrice((null != courseDTO.getDiscounts() && courseDTO.getDiscounts() > 0) ? new BigDecimal(courseDTO.getDiscounts()) : new BigDecimal(courseDTO.getPrice()));
        }
    	res.setSupportChannels(supportChannels);
    	return res;
	}
}
