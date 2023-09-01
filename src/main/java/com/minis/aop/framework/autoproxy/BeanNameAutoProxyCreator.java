package com.minis.aop.framework.autoproxy;

import com.minis.aop.AopProxyFactory;
import com.minis.aop.DefaultAopProxyFactory;
import com.minis.aop.PointcutAdvisor;
import com.minis.aop.ProxyFactoryBean;
import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.util.PatternMatchUtils;

/**
 * Bean 处理器：为名称能够匹配特定表达式的 bean 自动创建代理类
 */
public class BeanNameAutoProxyCreator implements BeanPostProcessor {

    /**
     * bean 名的匹配表达式
     */
    private String pattern;

    private BeanFactory beanFactory;

    private AopProxyFactory aopProxyFactory;

    private String interceptorName;

    private PointcutAdvisor advisor;

    public BeanNameAutoProxyCreator() {
        this.aopProxyFactory = new DefaultAopProxyFactory();
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public void setInterceptorName(String interceptorName) {
        this.interceptorName = interceptorName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * 核心方法：为 bean 创建代理对象，并返回这个代理对象
     * 在 bean 实例化之后、init 方法调用之前执行
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanNameAutoProxyCreator. try to create proxy for : " + beanName);
        if (isMatch(beanName, this.pattern)) {
            System.out.println(beanName + "BeanNameAutoProxyCreator. bean name matched, create proxy for " + bean);
            // 创建一个 ProxyFactoryBean
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(beanFactory);
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            return proxyFactoryBean;
        } else {
            return bean;
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

}