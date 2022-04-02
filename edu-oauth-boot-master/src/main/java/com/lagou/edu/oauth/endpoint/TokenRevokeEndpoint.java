package com.lagou.edu.oauth.endpoint;

import com.lagou.edu.common.result.ResponseDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 登出接口
 *
 **/
@FrameworkEndpoint
@Api(tags = "登出接口")
public class TokenRevokeEndpoint {

    @Autowired
    @Qualifier("consumerTokenServices")
    private ConsumerTokenServices tokenServices;

    @DeleteMapping("/oauth/token")
    @ApiOperation("退出登录")
    public ResponseDTO<String> deleteAccessToken(@RequestParam("access_token") String accessToken) {
        tokenServices.revokeToken(accessToken);
        return ResponseDTO.success();
    }

}
