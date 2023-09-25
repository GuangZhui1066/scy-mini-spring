package com.minis.beans.factory.support;

import com.minis.core.Resource;

/**
 * 解析并注册 BeanDefinition
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    int loadBeanDefinitions(Resource resource);

    int loadBeanDefinitions(Resource... resources);

    int loadBeanDefinitions(String location);

    int loadBeanDefinitions(String... locations);

}
