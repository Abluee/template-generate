package com.nbcb.template.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * 日志切面，记录请求和响应信息
 */
@Aspect
@Component
@Slf4j
public class LogAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.nbcb.template.controller..*.*(..))")
    public void controllerPointcut() {}

    @Around("controllerPointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        
        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        
        // 打印请求信息
        log.info("===================Request Begin===================");
        log.info("URL: {}", request.getRequestURL().toString());
        log.info("Method: {}", request.getMethod());
        log.info("IP: {}", request.getRemoteAddr());
        log.info("Class Method: {}.{}", 
                point.getSignature().getDeclaringTypeName(), 
                point.getSignature().getName());
        log.info("Args: {}", 
                objectMapper.writeValueAsString(Arrays.asList(point.getArgs())));
        
        // 执行目标方法
        Object result = point.proceed();
        
        // 打印响应信息
        log.info("===================Response Begin===================");
        log.info("Response: {}", 
                result != null ? objectMapper.writeValueAsString(result) : null);
        log.info("Time Cost: {}ms", System.currentTimeMillis() - beginTime);
        log.info("===================End===================");
        
        return result;
    }
}
