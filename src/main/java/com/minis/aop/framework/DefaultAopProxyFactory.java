package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        Class<?> targetClass = target.getClass();

        // 实现了接口的类，使用 JDK 动态代理
        if (targetClass.getInterfaces().length == 0) {
            return new CglibAopProxy(target, advisor);
        }
        // 没有实现接口的类，使用 Cglib 动态代理
        return new JdkDynamicAopProxy(target, advisor);
    }

}
