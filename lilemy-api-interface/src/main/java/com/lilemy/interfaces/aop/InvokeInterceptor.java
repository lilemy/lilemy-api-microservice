package com.lilemy.interfaces.aop;

import com.lilemy.common.service.InnerUserInterfaceInfoService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 接口调用 AOP
 **/
@Aspect
@Component
@Slf4j
public class InvokeInterceptor {

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;

    /**
     * 执行拦截
     */
    @Around("execution(* com.lilemy.interfaces.controller.*.*(..))")
    public Object doInterceptor(ProceedingJoinPoint point) throws Throwable {
        // 获取请求路径
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String url = httpServletRequest.getRequestURI();
        // 从请求头中获取用户和接口信息 id
        String userId = httpServletRequest.getHeader("user-id");
        String interfaceId = httpServletRequest.getHeader("interface-id");
        // 执行原方法
        Object result = point.proceed();
        // 接口调用成功扣减用户调用次数
        innerUserInterfaceInfoService.invokeInterface(Long.parseLong(interfaceId), Long.parseLong(userId));
        return result;
    }
}
