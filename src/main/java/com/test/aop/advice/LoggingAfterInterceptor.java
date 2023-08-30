package com.test.aop.advice;

import java.lang.reflect.Method;

import com.minis.aop.AfterReturningAdvice;

/**
 * 自定义的后置增强
 * 只需实现 AfterReturningAdvice
 */
public class LoggingAfterInterceptor implements AfterReturningAdvice {

    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("LoggingAfterInterceptor. afterReturning... ");
    }

}
