package com.minis.web.context.support;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.web.context.WebApplicationContext;

/**
 * IoC 容器 (AbstractApplicationContext)，同时增加了 ServletContext 属性，成为适用于 Web 场景的上下文 —— MVC 容器
 * MVC 容器中可以访问 IoC 容器 (因为 IoC 容器是它的父类)
 *
 * 子级上下文，负责创建 Controller 实例
 * 子级上下文可以访问到父级，父级不能访问子级
 */
public class AnnotationConfigWebApplicationContext extends AbstractRefreshableWebApplicationContext {

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        setConfigLocations(fileName);

        setParent(parentApplicationContext);

        setServletContext(((WebApplicationContext) getParent()).getServletContext());

        try {
            refresh();
        } catch (IllegalStateException | BeansException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        String[] configLocations = getConfigLocations();

        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(configLocations[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        loadBeanDefinitions(beanFactory, controllerNames);
    }

    private List<String> scanPackages(List<String> packages) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packages) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        List<String> tempControllerNames = new ArrayList<>();
        URL url = this.getClass().getClassLoader().getResource("/"+packageName.replaceAll("\\.", "/"));
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                tempControllerNames.addAll(scanPackage(packageName+"."+file.getName()));
            } else {
                String controllerName = packageName +"." +file.getName().replace(".class", "");
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory, List<String> controllerNames) {
        for (String controller : controllerNames) {
            String beanName = controller;
            String beanClassName = controller;
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClassName);
            beanFactory.registerBeanDefinition(beanName,beanDefinition);
        }
    }


    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        try {
            getBeanFactory().addBeanPostProcessor((BeanPostProcessor) (getBeanFactory().getBean("autowiredAnnotationBeanPostProcessor")));
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

}
