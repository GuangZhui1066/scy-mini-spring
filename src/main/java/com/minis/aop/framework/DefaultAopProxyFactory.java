package com.minis.aop.framework;

import com.minis.aop.PointcutAdvisor;

/**
 * 默认的 AOP 工厂类
 */
public class DefaultAopProxyFactory implements AopProxyFactory {

    /**
     * JDK 动态代理只能代理实现了接口的类，并且只能用接口来转换代理对象。否则就会类型转换报错: "com.sun.proxy.$Proxy cannot be cast to"
     *
     * 举例：
     *  AService 是接口，AServiceImpl 是实现类。被 JDK 动态代理。
     *  这样写就会报错:
     *      AServiceImpl aService = (AServiceImpl) applicationContext.getBean("aService");
     *  这样写则不报错:
     *      AService aService = (AService) applicationContext.getBean("aService");
     */
    @Override
    public AopProxy createAopProxy(Object target, PointcutAdvisor advisor) {
        Class<?> targetClass = target.getClass();

        // 实现了接口的类，使用 JDK 动态代理
        if (targetClass.getInterfaces().length == 0) {
            return new CglibAopProxy(target, advisor);
        }
        // 没有实现接口的类，使用 Cglib 动态代理
        return new JdkDynamicAopProxy(target, advisor);
    }

}
