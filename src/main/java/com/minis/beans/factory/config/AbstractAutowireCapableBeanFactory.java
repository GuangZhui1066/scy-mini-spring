package com.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.BeansException;
import com.minis.beans.factory.support.AbstractBeanFactory;

/**
 * AutowireCapableBeanFactory 的抽象实现类
 *
 * 继承了 AbstractBeanFactory，可以直接使用其创建bean、注册beanDefinition等方法
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /**
     * 所有的 bean 处理器
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws
        BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : this.getBeanPostProcessors()) {
            beanProcessor.setBeanFactory(this);
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : this.getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

}
