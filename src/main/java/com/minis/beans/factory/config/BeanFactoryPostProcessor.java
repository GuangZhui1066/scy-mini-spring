package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

/**
 * BeanDefinition 处理器
 * 和 BeanPostProcessor (Bean的处理器) 类似,可以对 BeanDefinition 进行处理
 *
 * 作用：
 *   在容器实际实例化 bean 之前，读取 BeanDefinition 进行处理，比如修改 BeanDefinition 中的属性值、添加属性等
 *   可以通过 order 属性来控制 BeanFactoryPostProcessor 的执行次序
 */
public interface BeanFactoryPostProcessor {

    void postProcessBeanFactory(BeanFactory beanFactory) throws BeansException;

}
