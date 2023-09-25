package com.minis.web.context;

import javax.servlet.ServletContext;

import com.minis.context.ConfigurableApplicationContext;

public interface ConfigurableWebApplicationContext extends WebApplicationContext, ConfigurableApplicationContext {

    void setServletContext(ServletContext servletContext);

    void setConfigLocations(String... configLocations);

    String[] getConfigLocations();

}
