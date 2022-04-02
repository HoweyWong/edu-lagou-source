package com.lagou.edu.front.pay.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.common.util.ValidateUtils;
import com.lagou.edu.front.common.UserManager;
import com.lagou.edu.front.pay.service.PayService;
import com.lagou.edu.front.pay.vo.request.GetPayInfoVo;
import com.lagou.edu.front.pay.vo.request.PayReqVo;
import com.lagou.edu.front.pay.vo.response.OrderVo;
import com.lagou.edu.front.pay.vo.response.PayInfoVo;
import com.lagou.edu.front.pay.vo.response.PayResVo;
import com.lagou.edu.front.utils.WebUtils;
import com.lagou.edu.pay.api.dto.CallBackReq;
import com.lagou.edu.pay.api.dto.CallBackRes;
import com.lagou.edu.pay.api.enums.Channel;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description:(支付控制器)   
 * @author: ma wei long
 * @date:   2020年6月16日 下午2:53:07
*/
@Slf4j
@RestController
@RequestMapping("/pay")
@Api(description = "支付下单相关接口", tags = "支付接口")
public class PayController {
	
    @Autowired
    private PayService payService;

    /**
     * @Description: (获取支付方式)   
     * @author: ma wei long
     * @date:   2020年6月21日 下午6:20:00   
    */
    @ApiOperation("获取支付方式")
    @GetMapping("/getPayInfo")
    public ResponseDTO<PayInfoVo> getPayInfo(@RequestParam("shopOrderNo") String shopOrderNo, HttpServletRequest request) {
    	log.info("getPayInfo - shopOrderNo:{}",shopOrderNo);
        return ResponseDTO.success(payService.getPayInfo(new GetPayInfoVo(shopOrderNo)));
    }
    /**
     * @Description: (创建订单)   
     * @author: ma wei long
     * @date:   2020年6月17日 下午3:33:18   
    */
    @ApiOperation("创建订单(发起支付)")
    @PostMapping("/saveOrder")
    public ResponseDTO<PayResVo> saveOrder(@RequestBody PayReqVo reqVo, HttpServletRequest request) {
    	log.info("saveOrder - reqVo:{}",JSON.toJSONString(reqVo));
    	reqVo.setUserid(UserManager.getUserId());
    	reqVo.setClientIp(WebUtils.getIpAddr(request));
        return ResponseDTO.success(payService.saveOrder(reqVo));
    }
    
    /**
     * @Description: (查询订单(支付结果))   
     * @author: ma wei long
     * @date:   2020年6月17日 下午3:42:12   
    */
    @ApiOperation("查询订单(支付结果)")
    @GetMapping("/getPayResult")
    public ResponseDTO<OrderVo> getPayResult(@RequestParam("orderNo") String orderNo) {
    	log.info("getOrder - orderNo:{}",orderNo);
        return ResponseDTO.success(payService.getPayResult(UserManager.getUserId(), orderNo));
    }
    
    /**
     * @Description: (微信支付回调)   
     * @author: ma wei long
     * @throws IOException 
     * @date:   2020年6月22日 下午3:50:49   
    */
    @ApiOperation(value = "微信支付回调")
    @PostMapping("/wxCallback")
    public String wxCallBack(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	InputStream inStream = null;
    	ByteArrayOutputStream outSteam = null;
    	String resultxml = null;
    	try {
    		inStream = request.getInputStream();
            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = inStream.read(buffer)) != -1) {
              outSteam.write(buffer, 0, len);
            }
            resultxml = new String(outSteam.toByteArray(), "utf-8");
		} catch (Exception e) {
			log.error("wxCallBack - err",e);
			ValidateUtils.isTrue(false, "回调处理失败");
		}finally {
			if(null != outSteam) {
				outSteam.close();
			}
			if(null != inStream) {
				inStream.close();
			}
		}
    	ValidateUtils.notNullParam(resultxml);
        log.info("wxCallback - 回调请求参数:{}",resultxml);
        CallBackReq req = new CallBackReq();
        req.setChannel(Channel.WECHAT.getName());
        req.setWxCallBackReqStr(resultxml);
        CallBackRes backRes = payService.callBack(req);
        log.info("wxCallback - 回调处理结果:" + JSON.toJSONString(backRes));
        return backRes.getResStr();
    }

    /**
     * @Description: (支付宝支付回调)   
     * @author: ma wei long
     * @date:   2020年6月22日 下午3:52:13   
    */
    @ApiOperation(value = "支付宝支付回调")
    @PostMapping("/zfbCallback")
    public String zfbCallBack(HttpServletRequest request) {
    	Map<String, String> params = new TreeMap<>();
        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            params.put(entry.getKey(), entry.getValue()[0]);
        }
        log.info("zfbCallBack - 回调请求参数:{}" + JSON.toJSONString(params));
        CallBackReq req = new CallBackReq();
        req.setChannel(Channel.ALIPAY.getName());
        req.setAliParams(params);
        CallBackRes backRes = payService.callBack(req);
        log.info("zfbCallBack - 回调处理结果:" + JSON.toJSONString(backRes));
        return backRes.getResStr();
    }
}
