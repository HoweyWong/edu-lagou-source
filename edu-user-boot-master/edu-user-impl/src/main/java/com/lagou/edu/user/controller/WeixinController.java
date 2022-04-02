package com.lagou.edu.user.controller;


import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.user.api.UserWeixinRemoteService;
import com.lagou.edu.user.api.dto.WeixinDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author leo
 * @since 2020-06-27
 */
@RestController
@RequestMapping("/user/weixin")
public class WeixinController {

    @Autowired
    private UserWeixinRemoteService userWeixinRemoteService;

    @GetMapping("/getUserWeixinByUserId")
    public WeixinDTO getUserWeixinByUserId(@RequestParam("userId") Integer userId) {
        return this.userWeixinRemoteService.getUserWeixinByUserId(userId);
    }

    @GetMapping("/getUserWeixinByOpenId")
    public WeixinDTO getUserWeixinByOpenId(@RequestParam("openId") String openId) {
        return this.userWeixinRemoteService.getUserWeixinByOpenId(openId);
    }

    @GetMapping("/getUserWeixinByUnionId")
    public WeixinDTO getUserWeixinByUnionId(@RequestParam("unionId") String unionId) {
        return this.userWeixinRemoteService.getUserWeixinByUnionId(unionId);
    }

    @PostMapping("/saveUserWeixin")
    public WeixinDTO saveUserWeixin(@RequestBody WeixinDTO weixinDTO) {
        return this.userWeixinRemoteService.saveUserWeixin(weixinDTO);
    }

    @PostMapping("/updateUserWeixin")
    public boolean updateUserWeixin(@RequestBody WeixinDTO weixinDTO) {
        return this.userWeixinRemoteService.updateUserWeixin(weixinDTO);
    }

    @PostMapping(value = "/bindUserWeixin")
    public ResponseDTO<WeixinDTO> bindUserWeixin(@RequestBody WeixinDTO weixinDTO) {
        return this.userWeixinRemoteService.bindUserWeixin(weixinDTO);
    }

    @PostMapping("/unBindUserWeixin")
    public boolean unBindUserWeixin(@RequestParam("userId") Integer userId) {
        return this.userWeixinRemoteService.unBindUserWeixin(userId);
    }
}