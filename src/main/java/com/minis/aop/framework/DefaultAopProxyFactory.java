package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }

}
