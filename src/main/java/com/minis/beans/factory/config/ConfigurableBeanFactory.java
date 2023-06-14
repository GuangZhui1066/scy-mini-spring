package com.minis.beans.factory.config;

import com.minis.beans.factory.BeanFactory;

/**
 * 抽象出维护bean之间依赖关系的能力，以及支持bean处理器的能力
 */
public interface ConfigurableBeanFactory extends BeanFactory, SingletonBeanRegistry {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);

    int getBeanPostProcessorCount();

    void registerDependentBean(String beanName, String dependentBeanName);

    String[] getDependentBeans(String beanName);

    String[] getDependenciesForBean(String beanName);

}

