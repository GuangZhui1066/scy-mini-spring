package com.minis.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单的 BeanFactory 实现类
 *
 * 继承 DefaultSingletonBeanRegistry，默认创建出的所有 bean 都是单例的.
 *
 * 实现了 BeanFactory 接口和 BeanDefinitionRegistry 接口，所以它即是 bean 的工厂也是 bean 的仓库。
 */
public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 用 ConcurrentHashMap，确保多线程并发情况下的安全性
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

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
            BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
            // 根据bean的定义创建bean的实例
            singleton = createBean(beanDefinition);
            // 把这个bean实例保存到bean的仓库中
            this.registerBean(beanName, singleton);
        }
        return singleton;
    }

    private Object createBean(BeanDefinition bd) {
        Object obj = null;
        try {
            // 根据类名获取 class 对象，然后创建对象实例
            obj = Class.forName(bd.getClassName()).newInstance();
        } catch (InstantiationException | ClassNotFoundException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return obj;

    }

    @Override
    public void registerBean(String beanName, Object beanObj) {
        this.registerSingleton(beanName, beanObj);
    }

    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return this.beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return this.beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionNames.add(beanName);
        this.beanDefinitionMap.put(beanName, beanDefinition);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionNames.remove(beanName);
        beanDefinitionMap.remove(beanName);
        this.removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

}
