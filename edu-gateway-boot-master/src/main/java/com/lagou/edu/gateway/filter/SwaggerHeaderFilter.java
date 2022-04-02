package com.lagou.edu.gateway.filter;

import com.lagou.edu.gateway.config.SwaggerProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//@Component
public class SwaggerHeaderFilter implements GlobalFilter, Ordered {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private static final String HEADER_NAME = "X-Forwarded-Prefix";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();
        if (!StringUtils.endsWithIgnoreCase(path, SwaggerProvider.API_URI)) {
            return chain.filter(exchange);
        }
        String basePath = path.substring(0, path.lastIndexOf(SwaggerProvider.API_URI));
        log.info("basePath: {}", basePath);
        ServerHttpRequest newRequest = request.mutate().header(HEADER_NAME, basePath).build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }

    @Override
    public int getOrder() {
        return -200;
    }
}