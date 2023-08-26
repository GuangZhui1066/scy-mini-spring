package com.minis.aop;

import com.minis.beans.factory.FactoryBean;
import com.minis.util.ClassUtils;

/**
 * 实现 FactoryBean，表示需要被代理的 bean
 */
public class ProxyFactoryBean implements FactoryBean<Object> {

    private AopProxyFactory aopProxyFactory;

    /**
     * 真实对象
     */
    private Object target;

    /**
     * 代理对象
     */
    private Object singletonInstance;

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


    /**
     * 获取代理对象
     */
    @Override
    public Object getObject() throws Exception {
        return getSingletonInstance();
    }

    private synchronized Object getSingletonInstance() {
        if (this.singletonInstance == null) {
            this.singletonInstance = getProxy(createAopProxy());
        }
        return this.singletonInstance;
    }

    protected AopProxy createAopProxy() {
        return getAopProxyFactory().createAopProxy(target);
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
