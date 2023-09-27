package com.minis.aop.framework;

import java.lang.reflect.Method;

import com.minis.aop.PointcutAdvisor;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

public class CglibAopProxy implements AopProxy {

    Object target;

    PointcutAdvisor advisor;

    public CglibAopProxy(Object target, PointcutAdvisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }


    @Override
    public Object getProxy() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(target.getClass());
        enhancer.setInterfaces(target.getClass().getInterfaces());
        enhancer.setCallback(new DynamicAdvisedInterceptor(target));
        return enhancer.create();
    }


    private static class DynamicAdvisedInterceptor implements MethodInterceptor {

        private final Object target;;

        private DynamicAdvisedInterceptor(Object target) {
            this.target = target;
        }

        @Override
        public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            return = methodProxy.invoke(target, args);
        }
    }

}
