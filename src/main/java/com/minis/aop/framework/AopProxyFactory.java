package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;

/**
 * AOP 的工厂类，负责创建 AOP 代理类
 */
public interface AopProxyFactory {

    AopProxy createAopProxy(Object target, PointcutAdvisor advisor);

}