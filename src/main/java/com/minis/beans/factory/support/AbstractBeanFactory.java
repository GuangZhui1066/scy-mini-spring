package com.minis.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.ConstructorArgumentValue;
import com.minis.beans.factory.config.ConstructorArgumentValues;

/**
 * 简单的 BeanFactory 实现类
 *
 * 继承 DefaultSingletonBeanRegistry，默认创建出的所有 bean 都是单例的.
 *
 * 实现了 BeanFactory 接口和 BeanDefinitionRegistry 接口，所以它即是 bean 的工厂也是 bean 的仓库。
 */
public class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {

    private List<String> beanDefinitionNames = new ArrayList<>();

    /**
     * 用 ConcurrentHashMap，确保多线程并发情况下的安全性
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    /**
     * 二级缓存
     *   存放已经实例化、但是有部分属性没有被赋值的 bean
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);


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
     */
    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接拿 bean 实例
        Object singleton = this.getSingleton(beanName);

        // 此时这个 bean 还没有实例化完成 (没有实例化对象 / 有实例化对象但是其所有属性没有被全部赋值)
        if (singleton == null) {
            // 从二级缓存中获取没有完全实例化的毛坯 bean
            singleton = this.earlySingletonObjects.get(beanName);

            // 二级缓存中没有，说明这个 bean 还没有被创建，就创建这个 bean
            if (singleton == null) {
                // 获取 bean 的定义
                BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
                // 根据bean的定义创建bean的实例
                singleton = createBean(beanDefinition);
                // 把这个bean实例保存到bean的仓库中
                this.registerBean(beanName, singleton);
            }
        }
        return singleton;
    }

    /**
     * 依赖注入(由框架而不是使用者来创建bean) 的原理是反射
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        // 使用构造器创建毛坯bean
        Object obj = doCreateBean(beanDefinition);

        // 把毛坯bean放入二级缓存
        this.earlySingletonObjects.put(beanDefinition.getName(), obj);

        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 为 bean 的属性赋值
        handleProperties(beanDefinition, clazz, obj);

        return obj;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Object obj = null;
        Class<?> clazz;
        Constructor<?> constructor;

        try {
            // 根据类名获取 class 对象
            clazz = Class.forName(beanDefinition.getClassName());

            // 处理构造器参数
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if (!constructorArgumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class[constructorArgumentValues.getArgumentCount()];
                Object[] paramValues = new Object[constructorArgumentValues.getArgumentCount()];

                for (int i = 0; i < constructorArgumentValues.getArgumentCount(); i++) {
                    ConstructorArgumentValue constructorArgumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
                    // 判断构造器参数的类型
                    if ("String".equals(constructorArgumentValue.getType()) || "java.lang.String".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    } else if ("Integer".equals(constructorArgumentValue.getType()) || "java.lang.Integer".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else if ("int".equals(constructorArgumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) constructorArgumentValue.getValue());
                    } else {
                        // 默认为 String 类型
                        paramTypes[i] = String.class;
                        paramValues[i] = constructorArgumentValue.getValue();
                    }
                }
                try {
                    // 根据构造器创建对象实例
                    constructor = clazz.getConstructor(paramTypes);
                    obj = constructor.newInstance(paramValues);
                } catch (InstantiationException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            } else {
                // 如果没有参数，调用无参构造器创建实例
                obj = clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {
        // 处理成员变量
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                // 对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                Class<?> paramType = null;
                Object paramValue = null;
                // 非引用类型的属性
                if (!propertyValue.isRef()) {
                    if ("String".equals(propertyValue.getType()) || "java.lang.String".equals(propertyValue.getType())) {
                        paramType = String.class;
                        paramValue = propertyValue.getValue();
                    } else if ("Integer".equals(propertyValue.getType()) || "java.lang.Integer".equals(propertyValue.getType())) {
                        paramType = Integer.class;
                        paramValue = Integer.valueOf((String)propertyValue.getValue());
                    } else if ("int".equals(propertyValue.getType())) {
                        paramType = int.class;
                        paramValue = Integer.valueOf((String)propertyValue.getValue());
                    } else {
                        // 默认为 String
                        paramType = String.class;
                        paramValue = propertyValue.getValue();
                    }
                }
                // 引用类型的属性
                else {
                    try {
                        paramType = Class.forName(propertyValue.getType());
                        paramValue = getBean((String) propertyValue.getValue());
                    } catch (ClassNotFoundException | BeansException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    // 查找此属性对应的 setter 方法，并调用 setter 方法为属性设置值
                    String methodName = "set" + propertyValue.getName().substring(0, 1).toUpperCase()
                        + propertyValue.getName().substring(1);
                    Method method = clazz.getMethod(methodName, paramType);
                    method.invoke(obj, paramValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void registerBean(String beanName, Object beanObj) {
        this.registerSingleton(beanName, beanObj);
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
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionNames.add(beanName);
        this.beanDefinitionMap.put(beanName, beanDefinition);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        beanDefinitionNames.remove(beanName);
        beanDefinitionMap.remove(beanName);
        this.removeSingleton(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.beanDefinitionMap.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return this.beanDefinitionMap.containsKey(beanName);
    }

}
