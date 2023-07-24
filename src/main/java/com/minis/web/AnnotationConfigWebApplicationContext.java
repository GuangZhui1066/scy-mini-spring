package com.minis.web;

import javax.servlet.ServletContext;

import com.minis.context.ClassPathXmlApplicationContext;

/**
 * IoC 容器 (ClassPathXmlApplicationContext)，同时增加了 ServletContext 属性，成为适用于 Web 场景的上下文 —— MVC 容器
 * MVC 容器中可以访问 IoC 容器 (因为 IoC 容器是它的父类)
 */
public class AnnotationConfigWebApplicationContext extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public AnnotationConfigWebApplicationContext(String fileName) {
        super(fileName);
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

}
