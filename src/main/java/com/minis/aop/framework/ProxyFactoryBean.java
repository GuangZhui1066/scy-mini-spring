package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.FactoryBean;
import com.minis.util.ClassUtils;

/**
 * 实现 FactoryBean，表示需要被代理的 bean
 */
public class ProxyFactoryBean implements FactoryBean<Object>, BeanFactoryAware {

    private AopProxyFactory aopProxyFactory;

    /**
     * 真实对象
     */
    private Object target;

    /**
     * 代理对象
     */
    private Object singletonInstance;

    /**
     * 通知
     */
    private PointcutAdvisor advisor;

    /**
     * 拦截器名称
     * 可以设置不同的拦截器，来实现不同的增强
     */
    private String interceptorName;

    private BeanFactory beanFactory;

    private ClassLoader proxyClassLoader = ClassUtils.getDefaultClassLoader();


    public ProxyFactoryBean() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setAopProxyFactory(AopProxyFactory aopProxyFactory) {
        this.aopProxyFactory = aopProxyFactory;
    }

    public AopProxyFactory getAopProxyFactory() {
        return this.aopProxyFactory;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


    /**
     * 获取代理对象
     */
    @Override
    public Object getObject() throws Exception {
        initializeAdvisor();
        return getSingletonInstance();
    }

    private synchronized void initializeAdvisor() {
        Object advice = null;

        try {
            advice = this.beanFactory.getBean(this.interceptorName);
        } catch (BeansException e) {
            e.printStackTrace();
        }

        this.advisor = (PointcutAdvisor) advice;
    }


    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(this.target, this.advisor);
    }

    /**
     * 生成代理对象
     */
    protected Object getProxy(AopProxy aopProxy) {
        return aopProxy.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

}
