package com.lilemy.gateway.filter;

import com.lilemy.common.model.entity.InterfaceInfo;
import com.lilemy.common.model.entity.User;
import com.lilemy.common.service.InnerInterfaceInfoService;
import com.lilemy.common.service.InnerUserInterfaceInfoService;
import com.lilemy.common.service.InnerUserService;
import com.lilemy.gateway.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * API接口鉴权过滤器
 */
@Slf4j
@Order(1)
@Component
public class InterfaceGlobalFilter implements GlobalFilter {

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest serverHttpRequest = exchange.getRequest();
        String path = serverHttpRequest.getPath().toString();
        String method = serverHttpRequest.getMethod().toString();
        if (!antPathMatcher.match("/api/interface/**", path)) {
            return chain.filter(exchange);
        }
        HttpHeaders headers = serverHttpRequest.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        ServerHttpResponse response = exchange.getResponse();
        // 获取调用用户
        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.error("getInvokeUser error", e);
        }
        if (invokeUser == null) {
            return handleNoAuth(response);
        }
        if (nonce != null && Long.parseLong(nonce) > 10000L) {
            return handleNoAuth(response);
        }
        // 时间和当前时间不能超过 5 分钟
        long currentTime = System.currentTimeMillis() / 1000;
        final long FIVE_MINUTES = 60 * 5L;
        if (timestamp != null && (currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            return handleNoAuth(response);
        }
        // 校验 secretKey
        String secretKey = invokeUser.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        if (sign == null || !sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        // 获取调用接口信息
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(path, method);
        } catch (Exception e) {
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null) {
            return handleNoFound(response);
        }
        // 判断用户是否还有调用次数
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = invokeUser.getId();
        Integer leftCount = innerUserInterfaceInfoService.invokeLeftCount(interfaceInfoId, userId);
        if (leftCount <= 0) {
            return handleNoAuth(response);
        }
        // 将调用用户和接口信息 id添加到请求头中
        ServerWebExchange build = exchange
                .mutate()
                .request(builder -> builder.header("user-id", String.valueOf(userId)))
                .request(builder -> builder.header("interface-id", String.valueOf(interfaceInfoId)))
                .build();
        return chain.filter(build);
    }

    public Mono<Void> handleNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleNoFound(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }
}