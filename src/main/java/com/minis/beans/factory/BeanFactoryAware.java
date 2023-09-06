package com.minis.beans.factory;

/**
 * 获取 beanFactory
 */
public interface BeanFactoryAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory);

}
