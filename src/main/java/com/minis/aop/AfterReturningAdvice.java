package com.minis.aop;

import java.lang.reflect.Method;

/**
 * 方法调用返回后的增强
 */
public interface AfterReturningAdvice extends AfterAdvice {

    /**
     * 参数 returnValue 表示方法调用后的返回值，在方法后置增强操作中可以拿到方法调用的返回值
     */
    void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable;

}
