package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.BeansException;
import com.minis.beans.factory.FactoryBean;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.BeanPostProcessor;
import com.minis.beans.factory.config.ConfigurableBeanFactory;

/**
 * BeanFactory 的抽象实现类
 *
 * 继承 DefaultSingletonBeanRegistry，默认创建出的所有 bean 都是单例的
 * 实现了 BeanFactory 接口和 BeanDefinitionRegistry 接口，所以它即是 bean 的工厂也是 bean 的仓库
 *
 * 作为抽象类，其提供了 BeanFactory, BeanDefinitionRegistry 等基础接口中 refresh(), getBean() 等方法的默认实现
 * 使得其他的实现类不需要再去实现这些方法
 */
public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    protected List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 用 ConcurrentHashMap，确保多线程并发情况下的安全性
     */
    protected Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 所有的 bean 处理器
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public AbstractBeanFactory() {
    }

    /**
     * 把注册过的所有 bean 都创建出来
     */
    public void refresh() {
        for (String beanName : beanDefinitionNames) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 容器的核心方法
     *
     * 创建bean的步骤：
     *   1. 调用构造器创建出bean的实例
     *   2. 把这个实例对象放入二级缓存
     *   3. 对这个实例对象的属性进行赋值 (包括定义在beans.xml中通过setter注入的属性，也包括被@Autowire修饰的自动注入的属性)
     *   4. 把构建完整的对象放入一级缓存
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接拿 bean 实例
        Object singleton = this.getSingleton(beanName);

        // 此时这个 bean 还没有实例化完成 (没有实例化对象 / 有实例化对象但是其所有属性没有被全部赋值)
        if (singleton == null) {
            BeanDefinition beanDefinition = getBeanDefinition(beanName);
            if (beanDefinition == null) {
                return null;
            }
            singleton = createBean(beanName, beanDefinition);
        }

        // 处理 FactoryBean
        if (singleton instanceof FactoryBean) {
            System.out.println("factory bean ------- " + beanName + " ------- " + singleton);
            return this.getObjectForBeanInstance(singleton, beanName);
        }
        else {
            System.out.println("normal bean ------- " + beanName + " ------- " + singleton);
        }

        return singleton;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException;

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
        Object object = getObjectFromFactoryBean(factory, beanName);
        return object;
    }


    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        return this.beanDefinitionMap.get(beanName).isSingleton();
    }

    @Override
    public boolean isPrototype(String beanName) {
        return this.beanDefinitionMap.get(beanName).isPrototype();
    }

    @Override
    public Class<?> getType(String beanName) {
        Object beanInstance = this.getSingleton(beanName);
        if (beanInstance != null) {
            return beanInstance.getClass();
        }
        return null;
    }


    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    @Override
    public int getBeanPostProcessorCount() {
        return this.beanPostProcessors.size();
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

}
