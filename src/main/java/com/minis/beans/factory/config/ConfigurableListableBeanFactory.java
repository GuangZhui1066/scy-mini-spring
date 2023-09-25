package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.ListableBeanFactory;

/**
 * BeanFactory 接口的集大成者，包含了 BeanFactory 体系的所有方法
 *
 * 整合
 *   ListableBeanFactory: 用集合管理 bean 和 BeanDefinition
 *   ConfigurableBeanFactory: 配置 BeanFactory
 *   AutowireCapableBeanFactory: 支持 @Autowire 注解
 */
public interface ConfigurableListableBeanFactory
    extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

}
