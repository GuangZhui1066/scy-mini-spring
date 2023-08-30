package com.test.aop.advice;

import java.lang.reflect.Method;

import com.minis.aop.MethodBeforeAdvice;

/**
 * 自定义的前置增强
 * 只需实现 MethodBeforeAdvice
 */
public class LoggingBeforeInterceptor implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) throws Throwable {
        System.out.println("LoggingBeforeInterceptor. before... ");
    }

}
