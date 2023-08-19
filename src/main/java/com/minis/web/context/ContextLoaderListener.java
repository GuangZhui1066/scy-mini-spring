package com.minis.web.context;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.minis.web.context.support.XmlWebApplicationContext;

/**
 * 定义监听器 Listener
 *
 *   1. Listener 需要实现 ServletContextListener 接口
 *   2. Listener 的初始化方法是 contextInitialized(ServletContextEvent event)，在其初始化方法中可以加入启动IoC容器的逻辑
 */
public class ContextLoaderListener implements ServletContextListener {

    /**
     * IoC 容器配置文件的路径
     */
    public static final String CONFIG_LOCATION_PARAM = "applicationContextConfigLocation";

    private WebApplicationContext context;

    public ContextLoaderListener() {
    }

    public ContextLoaderListener(WebApplicationContext context) {
        this.context = context;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }

    /**
     * 初始化方法
     */
    @Override
    public void contextInitialized(ServletContextEvent event) {
        initWebApplicationContext(event.getServletContext());
    }

    private void initWebApplicationContext(ServletContext servletContext) {
        // 获得上下文 ServletContext 中的配置文件路径 (即定义Service的 xml 文件)
        String contextLocation = servletContext.getInitParameter(CONFIG_LOCATION_PARAM);
        System.out.println("contextLocation-----------" + contextLocation);

        // 启动父级 IoC 容器，负责 Service 的创建
        WebApplicationContext wac = new XmlWebApplicationContext(contextLocation);
        wac.setServletContext(servletContext);
        this.context = wac;

        // 将 IoC 容器放入 ServletContext 的某个属性中
        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.context);
    }

}