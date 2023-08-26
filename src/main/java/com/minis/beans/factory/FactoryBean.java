package com.minis.beans.factory;

/**
 * Spring 中的 bean 有两类
 *  1. 一种是普通的 bean
 *  2. 一种是需要被代理的类，即 FactoryBean. FactoryBean 表示代理对象，其中包含了真实对象
 */
public interface FactoryBean<T> {

    /**
     * 获取内部包含的对象
     */
    T getObject() throws Exception;

    Class<?> getObjectType();

    default boolean isSingleton() {
        return true;
    }

}