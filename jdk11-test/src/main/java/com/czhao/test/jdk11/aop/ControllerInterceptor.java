package com.czhao.test.jdk11.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * @author zhaochun
 */
@Slf4j
@Aspect
public class ControllerInterceptor {
    @Pointcut("execution(public * com.czhao.test..*.*Controller.*(..))")
    protected void controllerLog() {
    }

    @Before("controllerLog()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.info(request.getRequestURL().toString() + " begin...");
    }

    @AfterReturning(pointcut = "controllerLog()", returning = "ret")
    public void doAfterReturning(JoinPoint joinPoint, Object ret) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return;
        }
        HttpServletRequest request = attributes.getRequest();
        log.info(request.getRequestURL().toString() + " end...");
    }
}
