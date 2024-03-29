package com.minis.beans.factory.xml;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;
import com.minis.beans.factory.support.AbstractBeanDefinitionReader;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.core.io.ClassPathXmlResource;
import com.minis.core.io.Resource;
import org.dom4j.Element;

/**
 * 解析XML文件中的 BeanDefinition
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    public XmlBeanDefinitionReader(DefaultListableBeanFactory beanFactory) {
        super(beanFactory);
    }


    /**
     * 解析资源中的 BeanDefinition 并注册
     */
    @Override
    public int loadBeanDefinitions(Resource resource) {
        while (resource.hasNext()) {
            Element element = (Element) resource.next();
            String beanName = element.attributeValue("name");
            String beanClassName = element.attributeValue("class");
            String initMethodName = element.attributeValue("init-method");
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClassName);
            beanDefinition.setInitMethodName(initMethodName);

            // 解析构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
            for (Element e : constructorElements) {
                String argumentName = e.attributeValue("name");
                String argumentType = e.attributeValue("type");
                String argumentValue = e.attributeValue("value");
                constructorArgumentValues.addArgumentValue(new ConstructorArgumentValue(argumentName, argumentType, argumentValue));
            }
            beanDefinition.setConstructorArgumentValues(constructorArgumentValues);

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
            // 这里不能用 (String[])refs.toArray()
            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            // 注册 beanDefinition 到仓库
            getRegistry().registerBeanDefinition(beanName, beanDefinition);
        }
        return 1;
    }

    @Override
    public int loadBeanDefinitions(String location) {
        // 将 XML 文件解析为资源
        Resource resource = new ClassPathXmlResource(location);
        return this.loadBeanDefinitions(resource);
    }

}