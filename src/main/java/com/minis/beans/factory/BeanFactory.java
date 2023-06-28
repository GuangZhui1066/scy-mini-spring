package com.minis.beans.factory;

import com.minis.beans.BeansException;

/**
 * IoC 容器 (bean的工厂类)
 *
 * 作用：
 *   将创建对象与使用对象的业务代码解耦，让业务开发人员无需关注底层对象(bean)的创建和生命周期
 *
 * 方法：
 *   方法一：获取一个 bean
 *   方法二：注册一个 bean
 */
public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    //void registerBean(String beanName, Object beanObj);

    Boolean containsBean(String beanName);

    boolean isSingleton(String beanName);

    boolean isPrototype(String beanName);

    Class<?> getType(String beanName);

}
