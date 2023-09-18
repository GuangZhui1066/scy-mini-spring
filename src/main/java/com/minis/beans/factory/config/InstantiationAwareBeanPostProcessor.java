package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValues;

/**
 * bean 实例化前后的处理器
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    /**
     * 在bean实例化之前执行
     *
     * 可以通过该方法自己创建 bean，而不走后面 Spring 创建 bean 的过程 (即 doCreateBean 方法)
     * 如果不创建 bean 则返回 null
     */
    Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    /**
     * bean实例化之后，设置属性之前执行
     *
     * 如果返回 false，就不对 bean 的属性赋值
     */
    boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException;

    /**
     * bean实例化之后，设置属性之前执行
     *
     * 处理 bean 的属性值
     */
    PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws BeansException;

}
