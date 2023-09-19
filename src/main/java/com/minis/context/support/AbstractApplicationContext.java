package com.minis.context.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minis.beans.BeansException;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.context.ApplicationEventPublisher;
import com.minis.core.env.Environment;

/**
 * ApplicationContext 的抽象实现类
 */
public abstract class AbstractApplicationContext implements ApplicationContext {

    private Environment environment;

    private ApplicationEventPublisher applicationEventPublisher;

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private long startupDate;

    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();


    /**
     * 核心方法
     */
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        prepareBeanFactory(this.getBeanFactory());

        // 注册并执行 BeanFactoryPostProcessor
        postProcessBeanFactory(this.getBeanFactory());
        // 注册 Bean后置处理器
        registerBeanPostProcessors(this.getBeanFactory());
        // 初始化事件发布者
        initApplicationEventPublisher();
        // 获取所有 bean
        onRefresh();
        // 注册事件监听者
        registerListeners();
        // 发布事件
        finishRefresh();
    }

    public abstract void registerListeners();
    public abstract void initApplicationEventPublisher();
    public abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);
    public abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);
    public abstract void onRefresh();
    public abstract void finishRefresh();

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    /**
     * 环境
     */
    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Environment getEnvironment() {
        return this.environment;
    }

    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public long getStartupDate() {
        return this.startupDate;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean isActive() {
        return true;
    }


    /**
     * 包装BeanFactory
     */
    @Override
    public abstract ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object returnObj = getBeanFactory().getBean(beanName);
        // todo 如何去掉下面这段逻辑
        // 这里需要再处理一下 ApplicationContextAware 接口，否则会有以下问题：
        //   父级上下文刷新时，会执行 initHandlerMappings() 方法时，会走到这里获取 RequestMappingHandlerMapping 这个bean，这个bean是在子级上下文刷新的时候创建的
        //   所以此时这个bean中的 applicationContext 是子级上下文。在后面执行 initMapping() 时，从子级上下文中拿不到 controller 的定义，因为 controller 的定义在父级上下文中
        //   所以这里需要将这个 bean 的 applicationContext 设置为当前的父级上下文
        if (returnObj instanceof ApplicationContextAware) {
            ((ApplicationContextAware) returnObj).setApplicationContext(this);
        }
        return returnObj;
    }

    @Override
    public Boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }
    //	public void registerBean(String beanName, Object obj) {
    //		getBeanFactory().registerBean(beanName, obj);
    //	}

    @Override
    public boolean isSingleton(String name) {
        return getBeanFactory().isSingleton(name);
    }

    @Override
    public boolean isPrototype(String name) {
        return getBeanFactory().isPrototype(name);
    }

    @Override
    public Class<?> getType(String beanName) {
        return getBeanFactory().getType(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBeanDefinition(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return getBeanFactory().getBeanDefinitionCount();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type) {
        return getBeanFactory().getBeanNamesForType(type);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }


    /**
     * bean处理器
     */
    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }


    /**
     * 事件
     */
    public ApplicationEventPublisher getApplicationEventPublisher() {
        return applicationEventPublisher;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

}
