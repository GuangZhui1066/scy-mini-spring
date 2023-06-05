package com.minis.beans.factory.config;

/**
 * 单例bean的注册表 (存放bean的仓库)
 *
 * 作用：
 *   管理单例的bean对象：注册、获取、判断是否存在
 */
public interface SingletonBeanRegistry {

    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

}
