package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.config.SingletonBeanRegistry;

/**
 * SingletonBeanRegistry 的默认实现类
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    protected List<String> beanNames = new ArrayList<>();

    protected Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

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
            this.singletonObjects.put(beanName, singletonObject);
            this.beanNames.add(beanName);
            System.out.println("bean registered. beanName: " + beanName);
        }
    }

    @Override
    public Object getSingleton(String beanName) {
        return this.singletonObjects.get(beanName);
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
