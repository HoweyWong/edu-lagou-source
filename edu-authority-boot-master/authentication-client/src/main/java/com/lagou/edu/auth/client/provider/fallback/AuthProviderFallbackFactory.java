package com.lagou.edu.auth.client.provider.fallback;

import com.lagou.edu.auth.client.dto.PermissionDTO;
import com.lagou.edu.auth.client.provider.AuthProvider;
import com.lagou.edu.common.entity.vo.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author : chenrg
 * @create 2020/7/17 10:25
 **/

@Slf4j
@Component
public class AuthProviderFallbackFactory implements FallbackFactory<AuthProvider> {

    @Override
    public AuthProvider create(Throwable throwable) {
        return new AuthProvider() {
            @Override
            public Result auth(String authentication, String userId, String url, String method) {
                log.error("Validate user access permission failed. userId:{}, url:{}", userId, url, throwable);
                return Result.fail();
            }

            @Override
            public Result<PermissionDTO> listUserPermission(Integer userId) {
                log.error("User query permission failed. userId:{}", userId, throwable);
                return Result.fail();
            }
        };
    }
}
