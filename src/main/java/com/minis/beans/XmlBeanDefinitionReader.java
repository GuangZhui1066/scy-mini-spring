package com.minis.beans;

import com.minis.core.Resource;
import org.dom4j.Element;

/**
 * 解析XML文件
 */
public class XmlBeanDefinitionReader {

    SimpleBeanFactory simpleBeanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
        this.simpleBeanFactory = simpleBeanFactory;
    }

    /**
     * 注册XML中配置的 BeanDefinition
     */
    public void loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanName = element.attributeValue("name");
            String beanClassName = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClassName);
            this.simpleBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}