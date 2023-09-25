package com.minis.beans.factory.support;

import com.minis.core.Resource;
import com.minis.core.env.Environment;
import com.minis.core.env.EnvironmentCapable;
import com.minis.core.env.StandardEnvironment;

public abstract class AbstractBeanDefinitionReader implements EnvironmentCapable, BeanDefinitionReader {

    private final BeanDefinitionRegistry registry;

    private Environment environment;

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;

        if (this.registry instanceof EnvironmentCapable) {
            this.environment = ((EnvironmentCapable) this.registry).getEnvironment();
        } else {
            this.environment = new StandardEnvironment();
        }
    }


    @Override
    public final BeanDefinitionRegistry getRegistry() {
        return this.registry;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }


    @Override
    public int loadBeanDefinitions(Resource... resources) {
        int counter = 0;
        for (Resource resource : resources) {
            counter += loadBeanDefinitions(resource);
        }
        return counter;
    }

    @Override
    public int loadBeanDefinitions(String... locations) {
        int counter = 0;
        for (String location : locations) {
            counter += loadBeanDefinitions(location);
        }
        return counter;
    }

}
