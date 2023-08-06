package com.minis.context;

import com.minis.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.beans.factory.xml.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

/**
 * Context 负责整合容器的启动过程
 *   1. 创建 BeanFactory
 *   1. 读XML文件，从中解析出Bean定义
 *   3. 实例化这些 bean，并将它们注入到 IoC 容器中
 *
 * 功能:
 *   识别XML文件中的Bean定义，创建Bean，并放入IoC容器中管理
 *   支持XML配置方式或者注解的方式进行Bean的依赖注入
 *   构建 BeanFactory 体系
 *   容器应用上下文
 *   事件机制
 */
public class ClassPathXmlApplicationContext extends AbstractApplicationContext {

    DefaultListableBeanFactory beanFactory;


    public ClassPathXmlApplicationContext(String fileName) {
        this(fileName, true);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) {
        Resource resource = new ClassPathXmlResource(fileName);
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        defaultListableBeanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        reader.loadBeanDefinitions(resource);
        this.beanFactory = defaultListableBeanFactory;

        if (isRefresh) {
            try {
                refresh();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRefresh() {
        this.beanFactory.refresh();
    }

    @Override
    public void finishRefresh() {
        publishEvent(new ContextRefreshEvent("Context Refreshed..."));
    }


    @Override
    public ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException {
        return this.beanFactory;
    }


    /**
     * 事件机制
     */
    @Override
    public void registerListeners() {
        ApplicationListener listener = new ApplicationListener();
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }

    @Override
    public void initApplicationEventPublisher() {
        ApplicationEventPublisher aep = new SimpleApplicationEventPublisher();
        this.setApplicationEventPublisher(aep);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        this.getApplicationEventPublisher().publishEvent(event);
    }

    @Override
    public void addApplicationListener(ApplicationListener listener) {
        this.getApplicationEventPublisher().addApplicationListener(listener);
    }


    /**
     * bean处理器
     */
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory bf) {

    }

    @Override
    public void registerBeanPostProcessors(ConfigurableListableBeanFactory bf) {
        this.beanFactory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
    }

}