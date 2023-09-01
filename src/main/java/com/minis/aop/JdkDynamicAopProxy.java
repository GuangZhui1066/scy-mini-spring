package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于 JDK 动态代理的 AOP 实现
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    Object target;

    PointcutAdvisor advisor;

    public JdkDynamicAopProxy(Object target, PointcutAdvisor advisor) {
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
        Class<?> targetClass = target == null ? null : target.getClass();

        // 不匹配切点规则，不执行方法和增强
        if (!this.advisor.getPointcut().getMethodMatcher().matches(method, targetClass)) {
            return null;
        }

        // 创建方法调用的实例
        ReflectiveMethodInvocation methodInvocation = new ReflectiveMethodInvocation(proxy, target, method, args);
        // 通过通知者获取到通知 (拦截器)
        MethodInterceptor methodInterceptor = this.advisor.getMethodInterceptor();
        // 用拦截器，执行方法调用以及增强操作
        return methodInterceptor.invoke(methodInvocation);
    }

}
