package com.lagou.edu.pay.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lagou.edu.common.constant.MQConstant;
import com.lagou.edu.common.key.IPKeyGenerator;
import com.lagou.edu.common.mq.RocketMqService;
import com.lagou.edu.common.mq.dto.BaseMqDTO;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.result.ResultCode;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.course.api.ActivityCourseRemoteService;
import com.lagou.edu.course.api.CourseRemoteService;
import com.lagou.edu.course.api.dto.ActivityCourseDTO;
import com.lagou.edu.course.api.dto.CourseDTO;
import com.lagou.edu.order.api.UserCourseOrderRemoteService;
import com.lagou.edu.order.api.dto.UserCourseOrderDTO;
import com.lagou.edu.order.api.enums.StatusTypeEnum;
import com.lagou.edu.order.api.enums.UserCourseOrderStatus;
import com.lagou.edu.pay.annotation.PayOrderRecord;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.dto.PayReqDTO;
import com.lagou.edu.pay.api.dto.PayResDTO;
import com.lagou.edu.pay.api.enums.Channel;
import com.lagou.edu.pay.api.enums.Currency;
import com.lagou.edu.pay.api.enums.OrderType;
import com.lagou.edu.pay.api.enums.Status;
import com.lagou.edu.pay.entity.PayOrder;
import com.lagou.edu.pay.mapper.PayOrderMapper;
import com.lagou.edu.pay.model.OutTrade;
import com.lagou.edu.pay.mq.dto.CancelPayOrderDTO;
import com.lagou.edu.pay.service.IPayOrderService;
import com.lagou.edu.pay.trade.ThirdPayService;
import com.lagou.edu.pay.trade.request.BasePayRequest;
import com.lagou.edu.pay.trade.request.aliPay.AliPayRequest;
import com.lagou.edu.pay.trade.request.wechatPay.WechatPayRequest;
import com.lagou.edu.pay.trade.response.BasePayResponse;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
//import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;

/**
 * @author: ma wei long
 * @date:   2020年6月17日 上午11:47:09
*/
@Slf4j
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements IPayOrderService {
	
	@Autowired
    private CourseRemoteService courseRemoteService;
	@Autowired
	private UserCourseOrderRemoteService userCourseOrderRemoteService;
	@Autowired
	private ThirdPayService thirdPayService;
	@Autowired
	private IPayOrderService payOrderService;
	@Autowired
	private RocketMqService rocketMqService;
	@Autowired
    private ActivityCourseRemoteService activityCourseRemoteService;
	
	/**
	 * @Description: (下单支付)   
	 * @author: ma wei long
	 * @date:   2020年6月18日 上午11:20:27   
	*/
	@Override
	public PayResDTO saveOrder(PayReqDTO reqDTO) {
		log.info("saveOrder - reqDTO:{}",JSON.toJSONString(reqDTO));
		
		ResponseDTO<UserCourseOrderDTO> resp = userCourseOrderRemoteService.getCourseOrderByOrderNo(reqDTO.getGoodsOrderNo());
		log.info("saveOrder - userCourseOrderRemoteService.getCourseOrderByOrderNo - GoodsOrderNo:{} resp:{}",reqDTO.getGoodsOrderNo(),JSON.toJSONString(resp));
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		
		CourseDTO courseDTO = courseRemoteService.getCourseById(resp.getContent().getCourseId(),reqDTO.getUserid());
		log.info("saveOrder - courseRemoteService.getCourseById - CourseId:{} courseDTO:{}",resp.getContent().getCourseId(),JSON.toJSONString(courseDTO));
		ValidateUtils.isTrue(null != courseDTO, "课程信息查询为空");
		
		PayResDTO res = new PayResDTO();
		//校验是否购买成功过
		PayOrder orderDB = checkSuccessBuyGoods(resp.getContent().getCourseId(), reqDTO.getUserid());
        if (null != orderDB) {
        	res.setOrderNo(orderDB.getOrderNo());
            res.setStatus(Status.PAY_SUCCESS.getCode());
            return res;
        }
        //查询最新的记录
        PayOrder lastPayOrder = getLastOrder(reqDTO.getUserid(), resp.getContent().getCourseId(),reqDTO.getChannel());
        if(null == lastPayOrder || !lastPayOrder.getStatus().equals(Status.NOT_PAY.getCode())) {
        	BigDecimal activityAmount = null;
        	if(resp.getContent().getActivityCourseId() > 0) {
        		//查询课程活动金额
        		ResponseDTO<ActivityCourseDTO> respAC = activityCourseRemoteService.getById(resp.getContent().getActivityCourseId());
        		ValidateUtils.isTrue(respAC.isSuccess(), respAC.getMessage());
        		activityAmount = new BigDecimal(respAC.getContent().getAmount());
        	}
        	//创建订单
        	lastPayOrder = payOrderService.saveOrder(reqDTO, courseDTO,activityAmount);
        }
        
        BasePayRequest request = new BasePayRequest();
        request.setOrder(lastPayOrder);
        request.setChannel(reqDTO.getChannel());
        request.setSource(reqDTO.getSource());
        //调用三方服务创建订单
        BasePayResponse payRes = thirdPayService.submitPay(request);
		log.info("saveOrder - thirdPayService.submitPay - request:{} payRes:{}",JSON.toJSONString(request),JSON.toJSONString(payRes));
		ValidateUtils.isTrue(payRes.isSuccess(), ResultCode.ALERT_ERROR.getState(),"提交三方支付服务异常");
		//延时消息 取消支付订单  TODO记得改下 延时级别
		rocketMqService.sendDelayed(MQConstant.Topic.CANCEL_PAY_ORDER, new BaseMqDTO<CancelPayOrderDTO>(new CancelPayOrderDTO(lastPayOrder.getId()),UUID.randomUUID().toString()), MQConstant.DelayLevel.level_18);
		res.setChannel(reqDTO.getChannel());
        res.setOrderNo(lastPayOrder.getOrderNo());
        res.setPayUrl(payRes.getUrl());
        res.setSource(reqDTO.getSource().getCode());
        res.setStatus(lastPayOrder.getStatus());
		return res;
	}
	
	
	/**
	 * @Description: (检查是否购买成功过该商品)   O
	 * @author: ma wei long
	 * @date:   2020年6月18日 上午11:23:27   
	*/
	PayOrder checkSuccessBuyGoods(Integer goodId,Integer userId) {
		return getOne(new QueryWrapper<PayOrder>().eq("product_id", goodId).eq("user_id", userId).eq("status", Status.PAY_SUCCESS));
	}
	
	/**
	 * @Description: (保存支付订单)   
	 * @author: ma wei long
	 * @date:   2020年6月18日 下午1:41:02   
	 */
	@Override
	@PayOrderRecord(type = StatusTypeEnum.INSERT)
	public PayOrder saveOrder(PayReqDTO reqDTO,CourseDTO courseDTO,BigDecimal activityAmount){
		PayOrder order = new PayOrder();
        order.setOrderNo(IPKeyGenerator.getInstance().generateKey().toString());
        //TODO 暂时写死1分钱
        order.setAmount(new BigDecimal(0.01));
//        if(null != activityAmount) {
//        	order.setAmount(activityAmount);
//        }else {
//          order.setAmount((null != courseDTO.getDiscounts() && courseDTO.getDiscounts() > 0) ? new BigDecimal(courseDTO.getDiscounts()) : new BigDecimal(courseDTO.getPrice()));
//        }
        order.setCount(1);
        order.setOrderType(OrderType.BUY_COURSE.getCode());
        order.setChannel(reqDTO.getChannel());
        order.setProductId(courseDTO.getId());
        order.setCurrency(Currency.GBEANS.name());
        order.setSource(reqDTO.getSource().getCode());
        order.setProductName(courseDTO.getCourseName());
        order.setStatus(Status.NOT_PAY.getCode());
        order.setGoodsOrderNo(reqDTO.getGoodsOrderNo());
        order.setCreatedTime(new Date());
        order.setUpdatedTime(order.getCreatedTime());
        order.setUserId(reqDTO.getUserid());
        order.setClientIp(reqDTO.getClientIp());
        save(order);
        return order;
	}

	/**
     * @Description: (支付结果回调通知)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午3:28:00   
    */
	@Override
    @GlobalTransactional(name="pay_callBack_tx",rollbackFor = Exception.class)
	public CallBackRes callBack(CallBackReq request) {
		String channelName = request.getChannel();
		BasePayResponse response = null;
		switch (Channel.ofName(channelName)) {
		case WECHAT:
			WechatPayRequest wechatPayRequest = new WechatPayRequest();
			wechatPayRequest.setParamStr(request.getWxCallBackReqStr());
			wechatPayRequest.setChannel(channelName);
			response = thirdPayService.callBack(wechatPayRequest);
			break;
		case ALIPAY:
			AliPayRequest aliPayRequest = new AliPayRequest();
			aliPayRequest.setChannel(channelName);
			aliPayRequest.setParams(request.getAliParams());
			response = thirdPayService.callBack(aliPayRequest);
			break;
		default:
			ValidateUtils.isTrue(false, "支付渠道错误");
		}
		
		OutTrade trade = response.getTrade();
		
		PayOrder payOrderDB = getOne(new QueryWrapper<PayOrder>().eq("order_no", trade.getOrderNo()));
		ValidateUtils.isTrue(null != payOrderDB, "支付订单信息查询为空");
		//更新商品订单
		if(Status.parse(trade.getStatus().getCode()) == Status.PAY_SUCCESS) {
			ResponseDTO<?> resp = userCourseOrderRemoteService.updateOrderStatus(payOrderDB.getGoodsOrderNo(), UserCourseOrderStatus.SUCCESS.getCode());
			log.info("callBack - userCourseOrderRemoteService.updateOrderStatus - GoodsOrderNo:{} resp:{}",payOrderDB.getGoodsOrderNo(),JSON.toJSONString(resp));
			ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		}
		//更新支付订单信息
		PayOrder updatePayOrder = new PayOrder();
		updatePayOrder.setId(payOrderDB.getId());
		updatePayOrder.setOutTradeNo(trade.getOutTradeNo());
		updatePayOrder.setStatus(trade.getStatus().getCode());
		updatePayOrder.setAmount(payOrderDB.getAmount());
		updatePayOrder.setOrderNo(payOrderDB.getOrderNo());
		if(trade.getStatus().equals(Status.PAY_SUCCESS)) {
			updatePayOrder.setPayTime(trade.getPayTime());
		}
		ValidateUtils.isTrue(payOrderService.updatePayOrderInfo(updatePayOrder), "支付订单更新失败");
		CallBackRes res = new CallBackRes();
		res.setResStr(response.getResStr());
		return res;
	}


	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
	@Override
	public PayOrder getLastOrder(Integer userId, Integer courseId,String channel) {
		List<PayOrder> payOrderList = list(new QueryWrapper<PayOrder>().eq("product_id", courseId).eq("user_id", userId).eq("channel", channel).orderByDesc("id"));
		if(null != payOrderList && payOrderList.size() > 0) {
			return payOrderList.get(0);
		}
		return null;
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
	@Override
	@PayOrderRecord(type = StatusTypeEnum.UPDATE)
	public boolean updatePayOrderInfo(PayOrder order) {
		return updateById(order);
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(name="cancelPayOrder_tx",rollbackFor = Exception.class)
	public void cancelPayOrder(CancelPayOrderDTO cancelPayOrderDTO) {
		PayOrder payOrderDB = this.getById(cancelPayOrderDTO.getOrderId());
		ValidateUtils.notNull(payOrderDB, ResultCode.ALERT_ERROR.getState(), "查询支付订单信息为空:orderId:" + cancelPayOrderDTO.getOrderId());
		
		if(!payOrderDB.getStatus().equals(Status.NOT_PAY.getCode())){
			//支付订单已经终态 直接返回
			log.warn("支付订单已经终态  payOrderDB:{}",JSON.toJSONString(payOrderDB));
			return;
		}
		ValidateUtils.isTrue(payOrderService.updateStatusInvalid(payOrderDB), "支付订单更新为失效失败");
		ResponseDTO<?> resp = userCourseOrderRemoteService.updateOrderStatus(payOrderDB.getGoodsOrderNo(), UserCourseOrderStatus.CANCEL.getCode());
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		//如果是活动商品还原库存
		ResponseDTO<?> respStock = activityCourseRemoteService.updateActivityCourseStock(payOrderDB.getProductId(),payOrderDB.getGoodsOrderNo());
		ValidateUtils.isTrue(respStock.isSuccess(), respStock.getState(),respStock.getMessage());
	}

	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	 */
	@Override
	@PayOrderRecord(type = StatusTypeEnum.CANCEL)
	@Transactional(rollbackFor = Exception.class)
	public boolean updateStatusInvalid(PayOrder payOrder) {
		PayOrder updatePayOrder = new PayOrder();
		updatePayOrder.setStatus(Status.INVALID.getCode());
		boolean res = this.update(updatePayOrder, new QueryWrapper<PayOrder>().eq("id", payOrder.getId()).eq("status", Status.NOT_PAY.getCode()));
		return res;
	}
	
	/**
	 * @author: ma wei long
	 * @date:   2020年7月28日 上午11:11:07   
	*/
	@Override
	@Transactional(rollbackFor = Exception.class)
    @GlobalTransactional(name="order_tx",rollbackFor = Exception.class)
	public void testTX() {
		//更新商品订单
		log.info("事务id1:" + RootContext.getXID());
		ResponseDTO<?> resp = userCourseOrderRemoteService.updateOrderStatus("494836834161917953", UserCourseOrderStatus.SUCCESS.getCode());
		ValidateUtils.isTrue(resp.isSuccess(), resp.getState(),resp.getMessage());
		
		//更新支付订单信息
		PayOrder updatePayOrder = new PayOrder();
		updatePayOrder.setId(1287958066096271361L);
		updatePayOrder.setStatus(2);
		updatePayOrder.setPayTime(new Date());
		ValidateUtils.isTrue(payOrderService.updatePayOrderInfo(updatePayOrder), "支付订单更新失败");
		
		if(true) {
			log.info("事务id2:" + RootContext.getXID());
			throw new RuntimeException("沙发沙发");
		}
	}
}
