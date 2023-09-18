package com.minis.aop.framework.autoproxy;

import java.util.HashSet;
import java.util.Set;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

/**
 * Bean 处理器：用于创建 AOP 代理代理对象
 */
public abstract class AbstractAutoProxyCreator implements SmartInstantiationAwareBeanPostProcessor, BeanFactoryAware {

    private BeanFactory beanFactory;

    /**
     * 记录已经生成过代理对象的 bean
     */
    private Set<Object> earlyProxyReferences = new HashSet<>();


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected BeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * 创建代理对象并返回
     *
     * 在 bean 创建完成后、进行初始化的最后一步时调用
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // 这个 bean 还没有被 AOP 处理过，为其生成代理对象，返回这个代理对象
        if (!earlyProxyReferences.contains(beanName)) {
            return createProxy(bean, beanName);
        }

        // 这个 bean 已经被 AOP 处理过，直接返回
        // todo 这里返回的是应该是真实对象还是代理对象 (目前版本返回的是真实对象)
        return bean;
    }

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName)
        throws BeansException {
        return pvs;
    }

    /**
     * 生成并暴露出代理对象，并记录这个 bean 已经创建了代理对象
     *
     * 当从第三级缓存中获取对象时调用
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        earlyProxyReferences.add(beanName);
        return createProxy(bean, beanName);
    }


    protected abstract Object createProxy(Object bean, String beanName);

}
