package com.minis.aop;

import java.lang.reflect.Method;

/**
 * 方法调用前的增强
 */
public interface MethodBeforeAdvice {

    void before(Method method, Object[] args, Object target) throws Throwable;

}
