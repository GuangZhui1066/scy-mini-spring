package com.minis.aop.framework.autoproxy;

import com.minis.aop.PointcutAdvisor;
import com.minis.aop.framework.AopProxyFactory;
import com.minis.aop.framework.DefaultAopProxyFactory;
import com.minis.aop.framework.ProxyFactoryBean;
import com.minis.beans.BeansException;
import com.minis.util.PatternMatchUtils;

/**
 * Bean 处理器：为名称能够匹配特定表达式的 bean 自动创建代理类
 */
public class BeanNameAutoProxyCreator extends AbstractAutoProxyCreator {

    /**
     * bean 名的匹配表达式
     */
    private String pattern;

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


    /**
     * 核心方法：为 bean 创建代理对象，并返回这个代理对象
     */
    @Override
    protected Object createProxy(Object bean, String beanName) {
        System.out.println("BeanNameAutoProxyCreator. try to create proxy for : " + beanName);
        if (isMatch(beanName, this.pattern)) {
            System.out.println(beanName + "BeanNameAutoProxyCreator. bean name matched, create proxy for " + bean);
            // 创建一个 ProxyFactoryBean
            ProxyFactoryBean proxyFactoryBean = new ProxyFactoryBean();
            proxyFactoryBean.setTarget(bean);
            proxyFactoryBean.setBeanFactory(getBeanFactory());
            proxyFactoryBean.setAopProxyFactory(aopProxyFactory);
            proxyFactoryBean.setInterceptorName(interceptorName);
            try {
                return proxyFactoryBean.getObject();
            } catch (Exception e) {
                throw new BeansException("FactoryBean threw exception on object[" + beanName + "] creation, e: " + e);
            }
        } else {
            return bean;
        }
    }

    protected boolean isMatch(String beanName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, beanName);
    }

}