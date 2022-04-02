package com.lagou.edu.gateway.service.impl;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.lagou.edu.auth.client.service.IAuthService;
import com.lagou.edu.gateway.service.IPermissionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class PermissionService implements IPermissionService {

    /**
     * 由authentication-client模块提供签权的feign客户端
     */
    @Autowired
    private IAuthService authService;

    @Override
    @Cached(name = "gateway_auth::", key = "#userId+#method+#url",
            cacheType = CacheType.LOCAL, expire = 10, timeUnit = TimeUnit.SECONDS, localLimit = 10000)
    public boolean permission(String authentication, String userId, String url, String method) {
        return authService.hasPermission(authentication, userId, url, method);
    }
}
