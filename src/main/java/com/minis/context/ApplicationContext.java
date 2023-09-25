package com.minis.context;

import com.minis.beans.factory.HierarchicalBeanFactory;
import com.minis.beans.factory.ListableBeanFactory;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
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

    /**
     * 获取父级上下文
     */
    ApplicationContext getParent();

    AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException;

}
