package com.minis.web.context.support;

import javax.servlet.ServletContext;

import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.context.support.AbstractRefreshableConfigApplicationContext;
import com.minis.web.context.ConfigurableWebApplicationContext;

public abstract class AbstractRefreshableWebApplicationContext extends AbstractRefreshableConfigApplicationContext
        implements ConfigurableWebApplicationContext {

    private ServletContext servletContext;


    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletContext getServletContext() {
        return this.servletContext;
    }


    @Override
    public String[] getConfigLocations() {
        return super.getConfigLocations();
    }

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        // todo
        //beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));
    }

}
