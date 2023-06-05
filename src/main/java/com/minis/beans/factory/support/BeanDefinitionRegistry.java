package com.minis.beans.factory.support;

import com.minis.beans.factory.config.BeanDefinition;

/**
 * BeanDefinition 的注册表
 *
 * 作用：
 *   存放BeanDefinition的仓库，可以添加、移除、获取、判断 BeanDefinition对象
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);

    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName);

    boolean containsBeanDefinition(String beanName);

}
