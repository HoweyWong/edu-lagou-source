package com.lagou.edu.gateway.filter;

import com.lagou.edu.auth.client.service.IAuthService;
import com.lagou.edu.gateway.service.IPermissionService;
import io.jsonwebtoken.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.LinkedHashSet;

/**
 * 请求url权限校验
 */
@Configuration
@ComponentScan(basePackages = "com.lagou.edu")
public class AccessGatewayFilter implements GlobalFilter {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String X_USER_ID = "x-user-id";
    private static final String X_USER_NAME = "x-user-name";
    private static final String X_USER_IP = "x-user-ip";

    private static final String BOSS_PATH_PREFIX = "/boss";

    /**
     * 由authentication-client模块提供签权的feign客户端
     */
    @Autowired
    private IAuthService authService;

    @Autowired
    private IPermissionService permissionService;

    /**
     * 1.首先网关检查token是否有效，无效直接返回401，不调用签权服务
     * 2.调用签权服务器看是否对该请求有权限，有权限进入下一个filter，没有权限返回403
     * <p>
     * 注： 前端会根据401去做登录跳转或更新token。而无权限返回403
     *
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String url = request.getPath().value();
        String authentication = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        log.info("Access filter. url:{}, access_token:{}", url, authentication);
        // 有authentication的情况下，判断登录状态及是否有权限
        if (StringUtils.isNotBlank(authentication)) {
            return validateAuthentication(exchange, chain, authentication, url);
        }
        // 不需要网关签权的url，直接返回
        if (authService.ignoreAuthentication(url)) {
            return chain.filter(exchange);
        }
        // 没有authentication且不是忽略权限验证的url，则返回401.
        return unauthorized(exchange);
    }

    /**
     * 有authentication字段的情况下，验证登录token。
     * 1. 如果解析token异常，返回401，前端重新跳转登录页。
     * 2. token状态正常，判断该url是否有权限，如果没有就返回403，前端根据403提示用户无权限访问接口。
     *
     * @param exchange
     * @param chain
     * @param authentication
     * @param url
     * @return
     */
    private Mono<Void> validateAuthentication(ServerWebExchange exchange, GatewayFilterChain chain, String authentication, String url) {
        ServerHttpRequest request = exchange.getRequest();
        String method = request.getMethodValue();
        String ip = request.getRemoteAddress().getAddress().getHostAddress();
        // 获取原始的url。
        LinkedHashSet<URI> originUrl = exchange.getRequiredAttribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR);
        // 处理jwt
        String userId = null;
        String userName = null;
        try {
            Jws<Claims> jwt = authService.getJwt(authentication);
            if (null != jwt && null != jwt.getBody()) {
                userId = (String) jwt.getBody().get("user_id");
                userName = (String) jwt.getBody().get("user_name");

                // 拼装用户id、用户名放到请求里面
                ServerHttpRequest.Builder builder = request.mutate();
                if (StringUtils.isNotBlank(userName)) {
                    builder.header(X_USER_NAME, userName);
                }
                if (StringUtils.isNotBlank(userId)) {
                    builder.header(X_USER_ID, userId);
                }
                if (StringUtils.isNotBlank(ip)) {
                    builder.header(X_USER_IP, ip);
                }
                exchange = exchange.mutate().request(builder.build()).build();
                log.info("userId:{}, userName:{}, access_token:{}, url:{}", userId, userName, authentication, url);
            }
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException var4) {
            log.error("user token error :{}", var4.getMessage());
            // 如果不是忽略url，则返回401，需要登录
            if (!authService.ignoreAuthentication(url)) {
                return unauthorized(exchange);
            }
        }

        // 如果是忽略的url，在填充header中的登录用户信息后直接返回
        if (authService.ignoreAuthentication(url)) {
            return chain.filter(exchange);
        }

        // 管理后台才需要权限，其他时候只判断jwt是否正常
        boolean hasPermission = true;
        if (isBossPath(originUrl, url)) {
            // 将原始url赋值给当前url。
            url = BOSS_PATH_PREFIX.concat(url);
            hasPermission = permissionService.permission(authentication, userId, url, method);
            log.info("Check boss permission. userId:{}, have permission:{}, url:{}, method:{}", userId, hasPermission, url, method);
        }

        if (hasPermission && StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userName)) {
            log.info("User can access. userId:{}, userName:{}, url:{}, method:{}", userId, userName, url, method);
            return chain.filter(exchange);
        }
        return forbidden(exchange);
    }

    /**
     * 根据原始url判断是否请求的后台管理功能url。
     * <p>filter中如果使用了StripPrefix，url会被截取前一个"/"节点。无法检测到url是否包含/boss。这里获取原始的url进行判断</p>
     *
     * @param originUrl 获取的原始url
     * @param url       通过一些filter处理过的url。
     * @return
     */
    private boolean isBossPath(LinkedHashSet<URI> originUrl, String url) {
        if (url.startsWith(BOSS_PATH_PREFIX)) {
            return true;
        }
        for (URI uri : originUrl) {
            if (uri.getPath().startsWith(BOSS_PATH_PREFIX)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 未通过权限验证，返回forbidden
     *
     * @param exchange
     * @return
     */
    private Mono<Void> forbidden(ServerWebExchange exchange) {
        return rebuildExchange(exchange, HttpStatus.FORBIDDEN);
    }

    /**
     * 未登录或token状态异常，返回401
     *
     * @param exchange
     */
    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        return rebuildExchange(exchange, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> rebuildExchange(ServerWebExchange exchange, HttpStatus httpStatus) {
        exchange.getResponse().setStatusCode(httpStatus);
        DataBuffer buffer = exchange.getResponse()
                .bufferFactory().wrap(httpStatus.getReasonPhrase().getBytes());
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
