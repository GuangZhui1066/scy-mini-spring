package com.minis.aop;

public class DefaultAdvisor implements Advisor {

    private MethodInterceptor methodInterceptor;

    public DefaultAdvisor() {
    }

    @Override
    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = methodInterceptor;
    }

    @Override
    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

    @Override
    public Advice getAdvice() {
        return methodInterceptor;
    }

}
