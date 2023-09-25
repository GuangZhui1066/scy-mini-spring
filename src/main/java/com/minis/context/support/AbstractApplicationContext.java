package com.minis.context.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.AutowireCapableBeanFactory;
import com.minis.beans.factory.config.BeanFactoryPostProcessor;
import com.minis.beans.factory.config.ConfigurableListableBeanFactory;
import com.minis.beans.factory.support.DefaultListableBeanFactory;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.context.ApplicationEvent;
import com.minis.context.ApplicationListener;
import com.minis.context.ConfigurableApplicationContext;
import com.minis.context.event.ApplicationEventMulticaster;
import com.minis.context.event.ContextRefreshedEvent;
import com.minis.context.event.SimpleApplicationEventMulticaster;
import com.minis.core.env.ConfigurableEnvironment;
import com.minis.core.env.Environment;
import com.minis.core.env.StandardEnvironment;

/**
 * ApplicationContext 的抽象实现类
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext {

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    private ApplicationContext parent;

    private ConfigurableEnvironment environment;

    // 事件发布者
    private ApplicationEventMulticaster applicationEventMulticaster;

    // 事件监听者
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();

    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();

    private long startupDate;

    private final AtomicBoolean active = new AtomicBoolean();
    private final AtomicBoolean closed = new AtomicBoolean();


    public AbstractApplicationContext() {
    }

    public AbstractApplicationContext(ApplicationContext parent) {
        this();
        setParent(parent);
    }


    @Override
    public String getApplicationName() {
        return "";
    }

    @Override
    public void setParent(ApplicationContext parent) {
        // 设置父级 ApplicationContext
        this.parent = parent;

        // 设置父级 BeanFactory
        this.getBeanFactory().setParentBeanFactory(getInternalParentBeanFactory());

        // 父级环境
        if (parent != null) {
            Environment parentEnvironment = parent.getEnvironment();
            if (parentEnvironment instanceof ConfigurableEnvironment) {
                getEnvironment().merge((ConfigurableEnvironment) parentEnvironment);
            }
        }
    }

    protected BeanFactory getInternalParentBeanFactory() {
        return (getParent() instanceof ConfigurableApplicationContext) ?
            ((ConfigurableApplicationContext) getParent()).getBeanFactory() : getParent();
    }

    @Override
    public ApplicationContext getParent() {
        return this.parent;
    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment = environment;
    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        if (this.environment == null) {
            this.environment = createEnvironment();
        }
        return this.environment;
    }

    protected ConfigurableEnvironment createEnvironment() {
        return new StandardEnvironment();
    }

    @Override
    public long getStartupDate() {
        return this.startupDate;
    }


    /**
     * 核心方法：容器刷新
     */
    @Override
    public void refresh() throws BeansException, IllegalStateException {
        // 初始化 BeanFactory，填入 BeanDefinitions
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        //
        prepareBeanFactory(beanFactory);

        // 注册 BeanFactoryPostProcessor
        postProcessBeanFactory(beanFactory);

        // todo
        invokeBeanFactoryPostProcessors(beanFactory);

        // 注册 BeanPostProcessor
        registerBeanPostProcessors(beanFactory);

        // 初始化事件发布者
        initApplicationEventMulticaster();

        // 注册事件监听者
        registerListeners();

        // todo: 初始化所有非懒加载的 bean
        finishBeanFactoryInitialization(beanFactory);

        // 发布事件
        finishRefresh();
    }


    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        // 创建 beanFactory，注册 beanDefinition
        refreshBeanFactory();
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        return beanFactory;
    }

    protected abstract void refreshBeanFactory();

    protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    }

    protected abstract void postProcessBeanFactory(ConfigurableListableBeanFactory bf);

    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
    }

    protected abstract void registerBeanPostProcessors(ConfigurableListableBeanFactory bf);

    /**
     * 初始化事件发布者
     */
    protected void initApplicationEventMulticaster() {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }

    /**
     * 注册事件监听者
     */
    protected void registerListeners() {
        for (ApplicationListener<?> listener : getApplicationListeners()) {
            getApplicationEventMulticaster().addApplicationListener(listener);
        }

        String[] beanNamesForType = getBeanNamesForType(ApplicationListener.class);
        for (String beanName : beanNamesForType) {
            try {
                Object bean = getBean(beanName);
                if (bean instanceof ApplicationListener) {
                    this.getApplicationEventMulticaster().addApplicationListener((ApplicationListener<?>) bean);
                }
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 初始化所有非懒加载的 bean
     */
    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
    }

    /**
     * 发布容器刷新完成事件
     */
    protected void finishRefresh() {
        publishEvent(new ContextRefreshedEvent(this));
    }


    @Override
    public BeanFactory getParentBeanFactory() {
        return getParent();
    }

    @Override
    public void close() {
        // todo: ContextClosedEvent
        //publishEvent(new ContextClosedEvent(this));
        closeBeanFactory();
        this.active.set(false);
    }

    protected abstract void closeBeanFactory();

    @Override
    public boolean isActive() {
        return this.active.get();
    }


    /**
     * 包装BeanFactory
     */
    @Override
    public abstract DefaultListableBeanFactory getBeanFactory() throws IllegalStateException;

    @Override
    public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException {
        return getBeanFactory();
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        Object returnObj = getBeanFactory().getBean(beanName);
        // todo 如何去掉下面这段逻辑
        // 这里需要再处理一下 ApplicationContextAware 接口，否则会有以下问题：
        //   子级上下文刷新时，会获取 RequestMappingHandlerMapping 这个bean (在执行initHandlerMappings方法时)，这个bean是在父级上下文刷新的时候创建的
        //   所以此时这个bean中的 applicationContext 是父级上下文。在后面执行 initMapping() 时，从父级上下文中拿不到 controller 的定义，因为 controller 的定义在子级上下文中
        //   所以这里需要将这个 bean 的 applicationContext 设置为当前的子级上下文
        if (returnObj instanceof ApplicationContextAware) {
            ((ApplicationContextAware) returnObj).setApplicationContext(this);
        }
        return returnObj;
    }

    @Override
    public Boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

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
    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }


    /**
     * 事件
     */
    ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
        if (this.applicationEventMulticaster == null) {
            throw new IllegalStateException("ApplicationEventMulticaster not initialized - " +
                "call 'refresh' before multicasting events via the context: " + this);
        }
        return this.applicationEventMulticaster;
    }

    public Collection<ApplicationListener<?>> getApplicationListeners() {
        return this.applicationListeners;
    }

    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.addApplicationListener(listener);
        } else {
            this.applicationListeners.add(listener);
        }
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

}
