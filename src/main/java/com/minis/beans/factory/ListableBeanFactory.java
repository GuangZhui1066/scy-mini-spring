package com.minis.beans.factory;

import java.util.Map;

import com.minis.beans.BeansException;

/**
 * 抽象出 BeanFactory 中用集合管理所有 bean 实例的能力，比如获取bean数量、获取所有bean的名字、按某个类型获取bean列表等
 */
public interface ListableBeanFactory extends BeanFactory {

    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(Class<?> type);

    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;

}
