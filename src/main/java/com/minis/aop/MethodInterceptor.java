package com.minis.aop;

/**
 * 对方法的拦截器，实现某个方法的执行以及增强
 */
public interface MethodInterceptor extends Interceptor {

    Object invoke(MethodInvocation invocation) throws Throwable;

}
