package com.minis.context;

public interface ConfigurableApplicationContext extends ApplicationContext {

    void addApplicationListener(ApplicationListener<?> listener);

}
