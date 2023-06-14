package com.minis.beans.factory.config;

import com.minis.beans.factory.ListableBeanFactory;

/**
 * 整合
 *   ListableBeanFactory: 用集合管理 bean 和 BeanDefinition
 *   ConfigurableBeanFactory: 维护 bean 之间的依赖关系
 *   AutowireCapableBeanFactory: 支持 @Autowire 注解
 */
public interface ConfigurableListableBeanFactory
    extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

}
