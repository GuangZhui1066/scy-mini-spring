package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;

public class CglibAopProxy implements AopProxy {

    Object target;

    PointcutAdvisor advisor;

    public CglibAopProxy(Object target, PointcutAdvisor advisor) {
        this.target = target;
        this.advisor = advisor;
    }

    @Override
    public Object getProxy() {
        return null;
    }

}
