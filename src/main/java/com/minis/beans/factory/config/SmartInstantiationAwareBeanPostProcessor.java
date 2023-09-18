package com.minis.beans.factory.config;

import com.minis.beans.BeansException;

public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {

    /**
     * 提前暴露 bean 的代理对象：
     *   在 bean 刚实例化的时候就将这个 bean 的代理对象暴露出去，而不是等 bean 初始化时再去创建它的代理对象
     *   避免了以下情况：
     *     当这个 bean 还没有初始化完成时，其他循环依赖的 bean 在创建时要来获取这个 bean，就只能拿到这个 bean 的真实对象而非代理对象
     *
     * Spring 中只有 AbstractAutoProxyCreator 实现了这个方法，实现的效果是返回这个 bean 的 AOP 代理对象
     */
    Object getEarlyBeanReference(Object bean, String beanName) throws BeansException;

}
