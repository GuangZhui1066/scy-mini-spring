package com.minis.beans.factory.config;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;

/**
 * 支持@Autowired功能的 bean 工厂
 *
 * 作用:
 *   在基础的 BeanFactory 的基础上添加了支持@Autowired注解的特性
 *   用于构造 —— 具有部分属性被 @Autowired 修饰的 bean
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;

}