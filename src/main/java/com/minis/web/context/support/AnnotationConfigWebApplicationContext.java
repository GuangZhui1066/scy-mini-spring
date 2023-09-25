package com.minis.web.context.support;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.support.AbstractApplicationContext;
import com.minis.web.context.WebApplicationContext;

/**
 * IoC 容器 (AbstractApplicationContext)，同时增加了 ServletContext 属性，成为适用于 Web 场景的上下文 —— MVC 容器
 * MVC 容器中可以访问 IoC 容器 (因为 IoC 容器是它的父类)
 *
 * 子级上下文，负责创建 Controller 实例
 * 子级上下文可以访问到父级，父级不能访问子级
 */
public class AnnotationConfigWebApplicationContext
        extends AbstractApplicationContext implements WebApplicationContext {

    private WebApplicationContext parentApplicationContext;

    private ServletContext servletContext;

    DefaultListableBeanFactory beanFactory;

    public AnnotationConfigWebApplicationContext(String fileName) {
        this(fileName, null);
    }

    public AnnotationConfigWebApplicationContext(String fileName, WebApplicationContext parentApplicationContext) {
        this.parentApplicationContext = parentApplicationContext;
        if (this.parentApplicationContext != null) {
            this.servletContext = this.parentApplicationContext.getServletContext();
        }

        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(fileName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        List<String> packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);
        List<String> controllerNames = scanPackages(packageNames);
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        this.beanFactory = bf;
        this.beanFactory.setParentBeanFactory(this.parentApplicationContext.getBeanFactory());
        loadBeanDefinitions(controllerNames);

        try {
            refresh();
        } catch (IllegalStateException | BeansException e) {
            e.printStackTrace();
        }
    }

    public void loadBeanDefinitions(List<String> controllerNames) {
        for (String controller : controllerNames) {
            String beanName = controller;
            String beanClassName = controller;
            BeanDefinition beanDefinition = new BeanDefinition(beanName, beanClassName);
            this.beanFactory.registerBeanDefinition(beanName,beanDefinition);
        }
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

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    protected void refreshBeanFactory() {
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {
    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        try {
            this.beanFactory.addBeanPostProcessor((BeanPostProcessor) (this.beanFactory.getBean("autowiredAnnotationBeanPostProcessor")));
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void closeBeanFactory() {
    }

    @Override
    public DefaultListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }

}
