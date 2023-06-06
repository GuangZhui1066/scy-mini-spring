package com.minis.beans.factory.config;

import com.minis.beans.BeansException;

/**
 * bean 的处理器 (在bean被创建之后处理bean)
 *
 * 作用：
 *   在 bean 被初始化之前/之后对 bean 进行处理
 */
public interface BeanPostProcessor {

    /**
     * 在 bean 被初始化之前处理 bean
     */
    Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException;

    /**
     * 在 bean 被初始化之后处理 bean
     */
    Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}

