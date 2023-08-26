package com.minis.aop;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target) {
        return new JdkDynamicAopProxy(target);
    }

}
