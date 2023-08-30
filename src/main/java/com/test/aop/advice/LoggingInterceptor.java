package com.test.aop.advice;

import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

/**
 * 日志增强：在方法调用前后都打印日志
 */
public class LoggingInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        System.out.println("LoggingInterceptor. method " + invocation.getMethod() + " is called on " +
            invocation.getThis() + " with args " + invocation.getArguments());

        Object result = invocation.proceed();

        System.out.println("LoggingInterceptor. method " + invocation.getMethod() + " returns " + result);

        return result;
    }

}
