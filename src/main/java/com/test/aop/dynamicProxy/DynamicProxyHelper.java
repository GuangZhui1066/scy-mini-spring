package com.test.aop.dynamicProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicProxyHelper {

    /**
     * 真实对象
     */
    private Object subject;

    public DynamicProxyHelper(Object subject) {
        this.subject = subject;
    }

    /**
     * 创建真实对象的代理对象
     */
    public Object getProxy() {
        return Proxy.newProxyInstance(
                DynamicProxyHelper.class.getClassLoader(),
                subject.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        // 前置增强
                        System.out.println("DynamicProxyHelper. before call real object........");
                        // 对真实对象进行方法调用（执行原本的代码逻辑）
                        Object result = method.invoke(subject, args);
                        // 后置增强
                        System.out.println("DynamicProxyHelper. after call real object. result : " + result + "........");
                        return result;
                    }
                }
        );
    }

}
