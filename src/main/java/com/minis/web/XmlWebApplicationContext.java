package com.minis.web;

import javax.servlet.ServletContext;

import com.minis.context.ClassPathXmlApplicationContext;

/**
 * 父级上下文，负责实例化 IoC 容器中的 Service
 */
public class XmlWebApplicationContext
        extends ClassPathXmlApplicationContext implements WebApplicationContext {

    private ServletContext servletContext;

    public XmlWebApplicationContext(String fileName) {
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
