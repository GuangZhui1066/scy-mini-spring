package com.minis.beans;

import java.util.List;

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

            // 解析构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            for (Element e : constructorElements) {
                String argumentName = e.attributeValue("name");
                String argumentType = e.attributeValue("type");
                String argumentValue = e.attributeValue("value");
                argumentValues.addArgumentValue(new ArgumentValue(argumentName, argumentType, argumentValue));
            }
            beanDefinition.setConstructorArgumentValues(argumentValues);

            // 注册 beanDefinition 到仓库
            this.simpleBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}