package com.minis.beans.factory.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.FactoryBean;

/**
 * 提供 FactoryBean 相关的方法
 */
public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    protected Object getObjectFromFactoryBean(FactoryBean factory, String beanName) {
        Object object = doGetObjectFromFactoryBean(factory, beanName);
        try {
            object = postProcessObjectFromFactoryBean(object, beanName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 从 FactoryBean 中获取内部包含的对象
     */
    private Object doGetObjectFromFactoryBean(final FactoryBean factory, final String beanName) {
        Object object = null;
        try {
            object = factory.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        return object;
    }

}
