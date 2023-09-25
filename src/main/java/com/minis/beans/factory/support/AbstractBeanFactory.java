package com.minis.beans.factory.support;

import java.util.ArrayList;
import java.util.List;

import com.minis.beans.BeansException;
import com.minis.beans.factory.BeanFactory;
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

    private BeanFactory parentBeanFactory;

    /**
     * 所有的 bean 处理器
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    public AbstractBeanFactory() {
    }


    @Override
    public Object getBean(String beanName) throws BeansException {
        return doGetBean(beanName);
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
    public Object doGetBean(String beanName) throws BeansException {
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
        return getObjectFromFactoryBean(factory, beanName);
    }


    @Override
    public Boolean containsBean(String beanName) {
        return containsSingleton(beanName);
    }

    @Override
    public boolean isSingleton(String beanName) {
        // todo
        //return this.beanDefinitionMap.get(beanName).isSingleton();
        return true;
    }

    @Override
    public boolean isPrototype(String beanName) {
        // todo
        //return this.beanDefinitionMap.get(beanName).isPrototype();
        return false;
    }

    @Override
    public Class<?> getType(String beanName) {
        Object beanInstance = getSingleton(beanName);
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


    @Override
    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        if (this.parentBeanFactory != null && this.parentBeanFactory != parentBeanFactory) {
            throw new IllegalStateException("Already associated with parent BeanFactory: " + this.parentBeanFactory);
        }
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override
    public BeanFactory getParentBeanFactory() {
        return this.parentBeanFactory;
    }

}
