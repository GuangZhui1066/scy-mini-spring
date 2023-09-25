package com.minis.context.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.support.DefaultListableBeanFactory;

public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

    private DefaultListableBeanFactory beanFactory;


    /**
     * 创建 BeanFactory，并注册 BeanDefinition
     */
    @Override
    protected final void refreshBeanFactory() throws BeansException {
        if (hasBeanFactory()) {
            destroyBeans();
            closeBeanFactory();
        }
        try {
            DefaultListableBeanFactory beanFactory = createBeanFactory();
            loadBeanDefinitions(beanFactory);
            this.beanFactory = beanFactory;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;


    @Override
    public final DefaultListableBeanFactory getBeanFactory() {
        return this.beanFactory;
    }

    protected final boolean hasBeanFactory() {
        return (this.beanFactory != null);
    }

    protected void destroyBeans() {
        // todo
        //getBeanFactory().destroySingletons();
    }

    @Override
    protected final void closeBeanFactory() {
        this.beanFactory = null;
    }

}
