package com.minis.context;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.minis.beans.BeanDefinition;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class ClassPathXmlApplicationContext {

    private List<BeanDefinition> beanDefinitions = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String fileName) {
        this.readXml(fileName);
        this.instanceBeans();
    }


    private void readXml(String fileName) {
        SAXReader saxReader = new SAXReader();
        try {
            // 类装载器负责从Java字符文件将字符流读入内存，并构造Class类对象，所以通过它可以得到一个文件的输入流
            URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
            Document document = saxReader.read(xmlPath);
            Element rootElement = document.getRootElement();
            for (Element element : rootElement.elements()) {
                String beanName = element.attributeValue("name");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClassName);
                beanDefinitions.add(beanDefinition);
            }
        } catch (Exception e) {}
    }

    private void instanceBeans() {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                singletons.put(beanDefinition.getName(), Class.forName(beanDefinition.getClassName()).newInstance());
            } catch (Exception e) {}
        }
    }

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

}
