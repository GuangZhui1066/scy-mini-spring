package com.minis.beans;

import com.minis.beans.factory.BeanFactory;

/**
 * 获取 beanFactory
 */
public interface BeanFactoryAware {

    void setBeanFactory(BeanFactory beanFactory);

}
