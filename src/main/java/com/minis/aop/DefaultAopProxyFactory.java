package com.minis.aop;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, Advisor advisor) {
        return new JdkDynamicAopProxy(target, advisor);
    }

}
