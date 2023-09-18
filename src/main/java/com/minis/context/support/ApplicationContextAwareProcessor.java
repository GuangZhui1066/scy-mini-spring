package com.minis.context.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.Aware;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.context.EnvironmentAware;

/**
 * 处理 ApplicationContextAware
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {


    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        invokeAwareInterfaces(bean);
        return bean;
    }

    private void invokeAwareInterfaces(Object bean) throws BeansException {
        if (bean instanceof Aware) {
            if (bean instanceof EnvironmentAware) {
                ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
            }
            if (bean instanceof ApplicationContextAware) {
                ((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
            }
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
