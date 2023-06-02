package com.minis.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 简单的 BeanFactory 实现类
 */
public class SimpleBeanFactory implements BeanFactory {

    private List<BeanDefinition> beanDefinitions = new ArrayList<>();

    private List<String> beanNames = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    public SimpleBeanFactory() {
    }


    /**
     * 容器的核心方法
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接拿 bean 实例
        Object singleton = singletons.get(beanName);

        // 此时还没有这个Bean的实例，需要创建实例
        if (singleton == null) {
            int i = beanNames.indexOf(beanName);
            // 这个 BeanDefinition 还没有被注册，就抛出异常
            if (i == -1) {
                throw new BeansException("bean is null.");
            }
            // 这个 BeanDefinition 已经被注册，获取其定义，创建 bean 实例
            else {
                BeanDefinition beanDefinition = beanDefinitions.get(i);
                try {
                    // 根据类名获取 class 对象，然后创建对象实例
                    singleton = Class.forName(beanDefinition.getClassName()).newInstance();
                } catch (Exception ignored) {}
                // 把这个bean实例保存到容器中
                singletons.put(beanDefinition.getName(), singleton);
            }
        }
        return singleton;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        this.beanDefinitions.add(beanDefinition);
        this.beanNames.add(beanDefinition.getName());
    }

}
