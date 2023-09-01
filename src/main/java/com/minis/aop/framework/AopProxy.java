package com.minis.aop.framework;

/**
 * AOP 代理类接口
 *
 * 可以有多种实现：
 *  1. JDK 动态代理
 *  2. Cglib 动态代理
 */
public interface AopProxy {

    Object getProxy();

}
