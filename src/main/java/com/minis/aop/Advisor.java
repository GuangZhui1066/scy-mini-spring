package com.minis.aop;

/**
 * 通知者 (顾问)，其持有通知 Advice
 */
public interface Advisor {

    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

}
