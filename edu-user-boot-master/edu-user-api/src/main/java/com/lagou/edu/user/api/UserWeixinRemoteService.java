package com.lagou.edu.user.api;

import com.lagou.edu.common.result.ResponseDTO;
import com.lagou.edu.user.api.dto.WeixinDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "${remote.feign.edu-user-boot.name:edu-user-boot}", path = "/user/weixin")
public interface UserWeixinRemoteService {

    @GetMapping("/getUserWeixinByUserId")
    WeixinDTO getUserWeixinByUserId(@RequestParam("userId") Integer userId);

    @GetMapping("/getUserWeixinByOpenId")
    WeixinDTO getUserWeixinByOpenId(@RequestParam("openId") String openId);

    @GetMapping("/getUserWeixinByUnionId")
    WeixinDTO getUserWeixinByUnionId(@RequestParam("unionId") String unionId);

    @PostMapping("/saveUserWeixin")
    WeixinDTO saveUserWeixin(@RequestBody WeixinDTO weixinDTO);

    @PostMapping("/updateUserWeixin")
    boolean updateUserWeixin(@RequestBody WeixinDTO weixinDTO);

    @PostMapping("/bindUserWeixin")
    ResponseDTO<WeixinDTO> bindUserWeixin(@RequestBody WeixinDTO weixinDTO);

    @PostMapping("/unBindUserWeixin")
    boolean unBindUserWeixin(@RequestParam("userId") Integer userId);
}
