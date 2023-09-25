package com.minis.context;

import java.io.Closeable;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.core.env.ConfigurableEnvironment;

/**
 * 在 ApplicationContext 的基础上增加了一些配置上下文的能力
 */
public interface ConfigurableApplicationContext extends ApplicationContext, Closeable {

    /**
     * 设置父级上下文
     */
    void setParent(ApplicationContext parent);

    /**
     * 设置环境
     */
    void setEnvironment(ConfigurableEnvironment environment);

    /**
     * 获取环境
     */
    @Override
    ConfigurableEnvironment getEnvironment();

    /**
     * 添加 Bean 处理器
     */
    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    /**
     * 添加事件监听者
     */
    void addApplicationListener(ApplicationListener<?> listener);

    /**
     * 容器刷新
     */
    void refresh() throws BeansException, IllegalStateException;

    @Override
    void close();

    boolean isActive();

    ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
