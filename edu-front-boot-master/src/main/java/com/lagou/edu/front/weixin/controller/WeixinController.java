package com.lagou.edu.front.weixin.controller;

import cn.hutool.core.util.URLUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lagou.edu.common.date.DateUtil;
import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.front.common.UserManager;
import com.lagou.edu.front.user.service.UserService;
import com.lagou.edu.user.api.UserWeixinRemoteService;
import com.lagou.edu.user.api.dto.WeixinDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpUserService;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/weixin/")
@Api(tags = "微信接口")
public class WeixinController {

    @Autowired
    private UserService userService;
    @Autowired
    private WxMpService wxMpService;
    @Autowired
    private WxMpUserService wxMpUserService;
    @Autowired
    private UserWeixinRemoteService userWeixinRemoteService;
    @Autowired
    private SymmetricCrypto crypto;

    @RequestMapping(value = "bind", method = RequestMethod.POST)
    @ApiOperation(value = "微信绑定--跳转到扫码页面", notes = "微信绑定--跳转到扫码页面")
    @ResponseBody
    public ResponseDTO bind() {
        // 获取用户id并加密处理，在回调的时候会用到
        Integer userId = UserManager.getUserId();
        String userIdBase64 = crypto.encryptBase64(userId.toString().getBytes());
        String redirectUrl = "http://edufront.lagou.com/front/weixin/callback?isBind=true&uid=" + userIdBase64;
        String url = wxMpService.buildQrConnectUrl(redirectUrl, WxConsts.QrConnectScope.SNSAPI_LOGIN, null);
        log.info("用户[{}]跳转到微信授权链接:{}", userId, url);
        return ResponseDTO.success(url);
//        return "redirect:" + url;
    }

    @RequestMapping(value = "login", method = RequestMethod.GET)
    @ApiOperation(value = "微信扫码登录", notes = "微信扫码登录")
    public String login() {
        String redirectUrl = "http://edufront.lagou.com/front/weixin/callback";
        String url = wxMpService.buildQrConnectUrl(redirectUrl, WxConsts.QrConnectScope.SNSAPI_LOGIN, null);
        log.info("微信登录跳转到微信授权链接:{}", url);
        return "redirect:" + url;
    }

    @RequestMapping(value = "callback", method = RequestMethod.GET)
    @ApiOperation(value = "微信绑定--授权回调", notes = "微信绑定--授权回调")
    public String callback(@RequestParam("code") String code, @RequestParam(required = false) String uid, @RequestParam(required = false) boolean isBind) {

        Integer userId = UserManager.getUserId();
        try {
            if ((null == userId || userId <= 0) && StringUtils.isNotBlank(uid)) {
                userId = Integer.parseInt(crypto.decryptStr(uid));
                log.info("userId解密, before:{}, after:{}", uid, userId);
            }
            // TODO 公众号需要微信认证、并且绑定微信开放平台
            // String json = "{\"openId\":\"oGYgl0u0vZMKVAByQ3hR0i7jpKew\",\"nickname\":\"leo\",\"sexDesc\":\"男\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"中国\",\"headImgUrl\":\"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83epKy1c3YeeI5vRqSxqDkaYc9XDuPao1BRLFKGf65SiaRIFqHTpeJg90RfrCXCo7WkicpfsPdKTdNTpA/132\",\"unionId\":\"oXEX_svcbl-mCDhWloqlEFNVHgP8\",\"privileges\":[]}";
            WxMpUser wxMpUser =
                    //GsonUtil.fromJson(json, WxMpUser.class);
                    wxMpService.oauth2getUserInfo(wxMpService.oauth2getAccessToken(code), null);
            log.info("=====微信用户信息:{}=====", wxMpUser);
            // 绑定的情况
            if (Boolean.TRUE.equals(isBind)) {
                if (null == userId || userId <= 0) {
                    // 绑定情况下用户未登录，跳转到首页
                    log.info("用户未登录，没办法绑定");
                    return "redirect:/";
                }

                String openId = wxMpUser.getOpenId();
                String unionId = wxMpUser.getUnionId();
                // 只有在用户将公众号绑定到微信开放平台帐号后，才会出现该字段
                if (StringUtils.isBlank(unionId)) {
                    // String unionJson = "{\"subscribe\":true,\"openId\":\"oZNu9wl74DhOaRUwUTbDpr8XmqBM\",\"nickname\":\"leo\",\"sexDesc\":\"男\",\"sex\":1,\"language\":\"zh_CN\",\"city\":\"\",\"province\":\"\",\"country\":\"中国\",\"headImgUrl\":\"http://thirdwx.qlogo.cn/mmopen/qE9MKluetOkLJcP2CIuJoa1ibQ4mXbIgdcJkphLLHO0jBRpXu3W0pY1ogicfstkm8FIQunfub5fSpzxcxEubNsbA/132\",\"subscribeTime\":1593333790,\"remark\":\"\",\"groupId\":0,\"tagIds\":[],\"subscribeScene\":\"ADD_SCENE_QR_CODE\",\"qrScene\":\"0\",\"qrSceneStr\":\"\"}";
                    WxMpUser wxMpUser1 = wxMpUserService.userInfo(openId);
                    unionId = wxMpUser1.getUnionId();
                    log.info("获取unionId[{}], openId[{}]", unionId, openId);
                }

                WeixinDTO weixin = this.userWeixinRemoteService.getUserWeixinByUserId(userId);
                if (null != weixin) {
                    if (StringUtils.isNotBlank(weixin.getOpenId()) && StringUtils.equals(weixin.getOpenId(), wxMpUser.getOpenId())) {
                        // 当前登录用户已绑定该微信
                        log.info("用户:{}, openId:{} 用户已绑定该微信", userId, wxMpUser.getOpenId());
                        return "redirect:http://edufront.lagou.com/#/setting";
                    } else {
                        return "redirect:http://edufront.lagou.com/#/setting?msg=" + URLUtil.encode("用户已绑定了其他微信账号");
                    }
                } else {
                    WeixinDTO weixinByOpenId = this.userWeixinRemoteService.getUserWeixinByOpenId(wxMpUser.getOpenId());
                    if (null == weixinByOpenId) {
                        // 开始绑定逻辑
                        WeixinDTO dto = new WeixinDTO();
                        dto.setCity(wxMpUser.getCity());
                        Date nowDate = DateUtil.getNowDate();
                        dto.setCreateTime(nowDate);
                        dto.setUpdateTime(nowDate);
                        dto.setUserId(userId);
                        dto.setSex(wxMpUser.getSex());
                        dto.setIsDel(false);
                        dto.setNickName(wxMpUser.getNickname());
                        dto.setOpenId(wxMpUser.getOpenId());
                        dto.setPortrait(wxMpUser.getHeadImgUrl());
                        dto.setUnionId(wxMpUser.getUnionId());
                        ResponseDTO<WeixinDTO> result = this.userWeixinRemoteService.bindUserWeixin(dto);
                        log.info("用户:{}, 绑定微信结果:{}", userId, null == result ? null : result.getContent());
                        if (null != result && !result.isSuccess()) {
                            return "redirect:http://edufront.lagou.com/#/setting?msg=" + URLUtil.encode("该微信账号已被其他用户绑定");
                        }
                        return "redirect:http://edufront.lagou.com/#/setting";
                    } else {
                        return "redirect:http://edufront.lagou.com/#/setting?msg=" + URLUtil.encode("该微信账号已被其他用户绑定");
                    }
                }
            } else {
                // 登录情况，涉及到oauth2登录, 获取到refresh_token，带到
                String refreshToken = this.userService.getRefreshTokenByWeixin(wxMpUser.getUnionId(), wxMpUser.getOpenId());
                return "redirect:http://edufront.lagou.com/#/setting?rtoken=" + (StringUtils.isNotBlank(refreshToken) ? refreshToken : StringUtils.EMPTY);
            }
        } catch (Exception e) {
            log.error("", e);
        }
        // 跳转到用户绑定页面
        return "redirect:http://edufront.lagou.com/#/setting";
    }

    @RequestMapping(value = "unBind", method = RequestMethod.GET)
    @ApiOperation(value = "微信解绑", notes = "微信解绑")
    @ResponseBody
    public ResponseDTO unBind() {
        Integer userId = UserManager.getUserId();
        WeixinDTO weixin = this.userWeixinRemoteService.getUserWeixinByUserId(userId);
        if (null == weixin) {
            log.info("用户[{}]没有绑定微信", userId);
            return ResponseDTO.response(201, "用户没有绑定微信");
        }
        this.userWeixinRemoteService.unBindUserWeixin(userId);
        log.info("用户[{}]已经解绑微信[{}]", userId, weixin.getUnionId());
        // 跳转到用户绑定页面
        return ResponseDTO.success();
    }
}
