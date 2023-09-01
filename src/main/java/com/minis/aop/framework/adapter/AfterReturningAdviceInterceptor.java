package com.minis.aop.framework.adapter;

import com.minis.aop.AfterReturningAdvice;
import com.minis.aop.MethodInterceptor;
import com.minis.aop.MethodInvocation;

public class AfterReturningAdviceInterceptor implements MethodInterceptor {

    private final AfterReturningAdvice advice;

    public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
        this.advice = advice;
    }


    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        Object retVal = mi.proceed();
        this.advice.afterReturning(retVal, mi.getMethod(), mi.getArguments(), mi.getThis());
        return retVal;
    }

}
