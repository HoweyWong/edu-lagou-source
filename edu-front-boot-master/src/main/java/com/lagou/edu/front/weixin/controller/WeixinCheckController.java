package com.lagou.edu.front.weixin.controller;

import com.lagou.edu.front.weixin.provider.WeixinMessageHandler;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@Slf4j
@RestController
@RequestMapping("/weixin/check/")
@Api(tags = "微信验证接口")
public class WeixinCheckController {

    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WeixinMessageHandler weixinMessageHandler;
    private WxMpMessageRouter wxMpMessageRouter;

    @PostConstruct
    public void init() {
        wxMpMessageRouter = new WxMpMessageRouter(wxMpService);
        wxMpMessageRouter
                .rule().async(false).handler(weixinMessageHandler).end();
    }

    @GetMapping(value = "signature")
    @ApiOperation(value = "验证消息", notes = "验证消息")
    public String auth(@RequestParam(value = "timestamp") String timestamp,
                       @RequestParam(value = "nonce") String nonce,
                       @RequestParam(value = "signature") String signature,
                       @RequestParam(value = "echostr", required = false) String echoStr,
                       @RequestParam(value = "msg_signature", required = false) String msgSignature,
                       @RequestParam(value = "encrypt_type", required = false) String encryptType,
                       @RequestBody(required = false) String requestBody) {

        log.info("timestamp={}&nonce={}&signature={}&echostr={}&msg_signature={}&encrypt_type={}",
                timestamp, nonce, signature, echoStr, msgSignature, encryptType);
        if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
            // 消息签名不正确，说明不是公众平台发过来的消息
            throw new IllegalArgumentException("非法请求");
        }

        boolean verifyRequest = StringUtils.isNotBlank(echoStr);
        if (verifyRequest) {
            // 说明是一个仅仅用来验证的请求，回显 echoStr
            return echoStr;
        }

        boolean notAes = !StringUtils.equalsIgnoreCase("aes", encryptType);
        if (notAes) {
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(requestBody);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            return outMessage == null ? StringUtils.EMPTY : outMessage.toXml();
        } else {
            WxMpConfigStorage config = wxMpService.getWxMpConfigStorage();
            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, config, timestamp, nonce, msgSignature);
            WxMpXmlOutMessage outMessage = wxMpMessageRouter.route(inMessage);
            return outMessage == null ? StringUtils.EMPTY : outMessage.toEncryptedXml(config);
        }
    }
}
