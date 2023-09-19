package com.minis.context;

import com.minis.beans.BeansException;
import com.minis.beans.factory.HierarchicalBeanFactory;
import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.core.env.Environment;
import com.minis.core.env.EnvironmentCapable;

/**
 * 上下文公共接口，所有的上下文都要实现 ApplicationContext
 * 是一个集成者，扩展了 BeanFactory 的功能
 *
 * 作用:
 *   支持上下文
 *   支持事件的发布与监听
 */
public interface ApplicationContext
    extends EnvironmentCapable, ListableBeanFactory, HierarchicalBeanFactory, ApplicationEventPublisher {

    String getApplicationName();

    long getStartupDate();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    void setEnvironment(Environment environment);

    @Override
    Environment getEnvironment();

    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() throws BeansException, IllegalStateException;

    void close();

    boolean isActive();

}
