package com.minis.beans;

import java.util.ArrayList;
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

            // 解析成员属性
            List<Element> propertyElements = element.elements("property");
            PropertyValues propertyValues = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String propertyName = e.attributeValue("name");
                String propertyType = e.attributeValue("type");
                String propertyValue = e.attributeValue("value");
                String propertyRef = e.attributeValue("ref");
                if (propertyRef == null || "".equals(propertyRef)) {
                    propertyValues.addPropertyValue(new PropertyValue(propertyName, propertyType, propertyValue, false));
                } else {
                    propertyValues.addPropertyValue(new PropertyValue(propertyName, propertyType, propertyRef, true));
                    refs.add(propertyRef);
                }
            }
            beanDefinition.setPropertyValues(propertyValues);
            // toArray(new String[0]) 表示根据参数数组的类型，构造了一个相同类型的，长度跟refs一致的空数组
            // 这里不能用 (String[])refs.toArray(
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            // 注册 beanDefinition 到仓库
            this.simpleBeanFactory.registerBeanDefinition(beanName, beanDefinition);
        }
    }

}