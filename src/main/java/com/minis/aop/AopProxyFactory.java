package com.minis.aop;

/**
 * AOP 的工厂类，负责创建 AOP 代理类
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(Object target, PointcutAdvisor advisor);

}