package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于 JDK 动态代理的 AOP 实现
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    Object target;

    Advisor advisor;

    public JdkDynamicAopProxy(Object target, Advisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        Object obj = Proxy.newProxyInstance(
            JdkDynamicAopProxy.class.getClassLoader(),
            target.getClass().getInterfaces(),
            this);
        return obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 创建方法调用的实例
        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(target, method, args);
        // 通过顾问获取到拦截器 (通知)
        MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
        // 用拦截器，执行方法调用以及增强操作
        return methodInterceptor.invoke(methodInvocation);
    }

}
