package com.minis.web.context;

import javax.servlet.ServletContext;

import com.minis.context.ApplicationContext;

/**
 * Web 应用的上下文
 */
public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    /**
     * 获取 Servlet 容器的上下文 —— ServletContext
     */
    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);

}
