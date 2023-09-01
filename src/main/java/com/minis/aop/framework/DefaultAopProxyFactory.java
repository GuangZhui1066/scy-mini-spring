package com.minis.aop.framework;

import java.lang.reflect.Proxy;

import com.minis.aop.PointcutAdvisor;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        //Class<?> targetClass = target.getClass();
        //
        //if (targetClass.isInterface()|| Proxy.isProxyClass(targetClass)) {
        //    return new JdkDynamicAopProxy(target, advisor);
        //}
        //return new CglibAopProxy(target, advisor);

        return new JdkDynamicAopProxy(target, advisor);
    }

}
