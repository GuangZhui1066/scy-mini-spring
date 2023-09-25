package com.minis.web.context.support;

import com.minis.beans.BeansException;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * 父级上下文，负责实例化 IoC 容器中的 Service
 */
public class XmlWebApplicationContext extends AbstractRefreshableWebApplicationContext {

    public XmlWebApplicationContext(String fileName) {
        setConfigLocations(fileName);

        try {
            refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

}
