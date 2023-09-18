package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.ObjectFactory;
import com.minis.beans.factory.config.SingletonBeanRegistry;

/**
 * SingletonBeanRegistry 的默认实现类
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected List<String> beanNames = new ArrayList<>();

    /**
     * 一级缓存
     *   存放已经构建完成的 bean（如果是被代理的 bean，就存放代理对象）
     *   二级缓存和三级缓存中存放的都是还在创建过程中的 bean
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<String, Object>(256);

    /**
     * 二级缓存
     *   存放已经用构造器(有参构造器或无参构造器)实例化、但是有部分属性没有被赋值的 bean
     *   如果 bean 被代理，那么二级缓存中存放的是该 bean 的代理对象
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    /**
     * 三级缓存
     *   存放对象工厂。bean 在被实例化之后，其对象工厂就会被放入三级缓存
     */
    private Map<String, ObjectFactory<?>> singletonFactories = new HashMap<String, ObjectFactory<?>>();


    /**
     * 保存 Bean 的包含关系：key 是 Bean 的名称，value 是 Bean 里面包含的其它 Bean 的名称集合
     */
    protected Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    /**
     * 保存 Bean 的依赖关系：key 是 Bean 的名称，value 是依赖于该 Bean 的其它 Bean 的名称集合
     */
    protected Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    /**
     * 保存 Bean 的依赖关系：key 是 Bean 的名称，value 是该 Bean 所依赖的其它 Bean 名称集合
     */
    protected Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            Object oldObject = this.singletonObjects.get(beanName);
            if (oldObject != null) {
                throw new IllegalStateException("Could not register object [" + singletonObject +
                    "] under bean name '" + beanName + "': there is already object [" + oldObject + "] bound");
            }
            addSingleton(beanName, singletonObject);
            System.out.println("bean registered. beanName: " + beanName);
        }
    }

    /**
     * 将构建完成的 bean 放入一级缓存，并从二三级缓存中移除
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
        }
    }

    /**
     * 从一二三级缓存中获取 bean
     *
     * 1. 没有循环依赖时 bean 正常的生命周期
     * 如果没有循环依赖，getSingleton() 方法执行时就不会从三级缓存中获取到 singletonFactory，也不会执行其 getObject() 方法
     * 这种情况下 bean 就是正常的生命周期：
     *   实例化 -> 属性赋值 -> 初始化（在初始化过程中 BeanPostProcessor 会执行 postProcessAfterInitialization() 方法，如果这个 bean 被代理，那么 AbstractAutoProxyCreator 就会进行 AOP 创建代理对象来代替真实对象）
     *
     * 2. 有循环依赖时 bean 的生命周期
     * 只有在存在循环依赖的情况下，在对 bean 进行属性赋值的过程中，就会从三级缓存中获取到还未完全创建的 bean 的 singletonFactory，并且会执行其 getObject() 方法
     * 如果这个未完全创建的 bean 被代理了，那么执行 getObject() 方法时 AbstractAutoProxyCreator 会调用 getEarlyBeanReference() 方法进行 AOP 创建代理对象
     * 当这个 bean 进行到初始化执行 postProcessAfterInitialization() 这一步时，就会发现已经进行过 AOP (earlyProxyReferences 已经标记过)
     *
     * todo： 在没有循环依赖的情况下，是否会调用 singletonFactory.getObject()，spring 中是怎样的
     */
    @Override
    public Object getSingleton(String beanName) {
        Object singletonObject = singletonObjects.get(beanName);

        if (singletonObject == null) {
            // 此时这个 bean 还没有实例化完成 (没有实例化对象 / 有实例化对象但是其所有属性没有被全部赋值)
            // 从二级缓存中获取没有完全实例化的毛坯 bean
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    // 从三级缓存中获取这个 bean 的 ObjectFactory 对象，通过 ObjectFactory 获取到这个 bean 的代理对象
                    singletonObject = singletonFactory.getObject();
                    // 然后把这个 bean 的代理对象放入二级缓存，供依赖方获取
                    // 如果这里没有二级缓存，每次都从三级缓存中获取 ObjectFactory 并执行 getObject() 来获取代理类，那么每次获取到的都是不同的代理对象
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        singletonFactories.put(beanName, singletonFactory);
    }

    @Override
    public boolean containsSingleton(String beanName) {
        return this.singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        return this.beanNames.toArray(new String[0]);
    }

    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.beanNames.remove(beanName);
        }
    }

    /**
     * 保存具有依赖关系的 Bean
     */
    public void registerDependentBean(String beanName, String dependentBeanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans != null && dependentBeans.contains(dependentBeanName)) {
            return;
        }

        // No entry yet -> fully synchronized manipulation of the dependentBeans Set
        synchronized (this.dependentBeanMap) {
            dependentBeans = this.dependentBeanMap.get(beanName);
            if (dependentBeans == null) {
                dependentBeans = new LinkedHashSet<>(8);
                this.dependentBeanMap.put(beanName, dependentBeans);
            }
            dependentBeans.add(dependentBeanName);
        }
        synchronized (this.dependenciesForBeanMap) {
            Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(dependentBeanName);
            if (dependenciesForBean == null) {
                dependenciesForBean = new LinkedHashSet<>(8);
                this.dependenciesForBeanMap.put(dependentBeanName, dependenciesForBean);
            }
            dependenciesForBean.add(beanName);
        }
    }

    public boolean hasDependentBean(String beanName) {
        return this.dependentBeanMap.containsKey(beanName);
    }

    public String[] getDependentBeans(String beanName) {
        Set<String> dependentBeans = this.dependentBeanMap.get(beanName);
        if (dependentBeans == null) {
            return new String[0];
        }
        return (String[]) dependentBeans.toArray();
    }

    public String[] getDependenciesForBean(String beanName) {
        Set<String> dependenciesForBean = this.dependenciesForBeanMap.get(beanName);
        if (dependenciesForBean == null) {
            return new String[0];
        }
        return dependenciesForBean.toArray(new String[0]);
    }

    /**
     * 保存具有包含关系的 Bean（内部类与外部类）
     */
    public void registerContainedBean(String containedBeanName, String containingBeanName) {
        synchronized (this.containedBeanMap) {
            Set<String> containedBeans =
                this.containedBeanMap.computeIfAbsent(containingBeanName, k -> new LinkedHashSet<>(8));
            if (!containedBeans.add(containedBeanName)) {
                return;
            }
        }
        registerDependentBean(containedBeanName, containingBeanName);
    }

}
