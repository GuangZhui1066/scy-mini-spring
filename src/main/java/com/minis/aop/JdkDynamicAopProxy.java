package com.minis.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 基于 JDK 动态代理的 AOP 实现
 */
public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    Object target;

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
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
        // 事务处理
        System.out.println("JdkDynamicAopProxy. before call real object........");
        Object result = method.invoke(target, args);
        // 打印日志、统计接口耗时
        System.out.println("JdkDynamicAopProxy. after call real object........");
        return result;
    }

}
