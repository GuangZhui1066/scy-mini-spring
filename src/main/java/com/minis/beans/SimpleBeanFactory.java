package com.minis.beans;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的 BeanFactory 实现类
 *
 * 继承 DefaultSingletonBeanRegistry，默认创建出的所有 bean 都是单例的.
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    /**
     * 用 ConcurrentHashMap，确保多线程并发情况下的安全性
     */
    private Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>(256);

    public SimpleBeanFactory() {
    }


    /**
     * 容器的核心方法
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接拿 bean 实例
        Object singleton = this.getSingleton(beanName);

        // 此时还没有这个 bean 的实例，需要创建实例
        if (singleton == null) {
            // 获取 bean 的定义
            BeanDefinition beanDefinition = this.beanDefinitions.get(beanName);
            if (beanDefinition == null) {
                throw new BeansException("No bean.");
            }

            // 根据bean的定义创建bean的实例
            try {
                // 根据类名获取 class 对象，然后创建对象实例
                singleton = Class.forName(beanDefinition.getClassName()).newInstance();
            } catch (Exception ignored) {}

            // 把这个bean实例保存到容器中
            singletons.put(beanDefinition.getName(), singleton);
        }
        return singleton;
    }

    @Override
    public void registerBean(String beanName, Object beanObj) {
        this.registerSingleton(beanName, beanObj);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

}
