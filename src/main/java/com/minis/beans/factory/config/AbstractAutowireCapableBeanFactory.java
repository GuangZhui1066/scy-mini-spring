package com.minis.beans.factory.config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.minis.beans.BeansException;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;
import com.minis.beans.factory.Aware;
import com.minis.beans.factory.BeanFactoryAware;
import com.minis.beans.factory.BeanNameAware;
import com.minis.beans.factory.InitializingBean;
import com.minis.beans.factory.support.AbstractBeanFactory;
import com.minis.util.ReflectionUtils;

/**
 * AutowireCapableBeanFactory 的抽象实现类
 *
 * 继承了 AbstractBeanFactory，可以直接使用其创建bean、注册beanDefinition等方法
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        return doCreateBean(beanName, beanDefinition);
    }


    protected Object doCreateBean(String beanName, BeanDefinition beanDefinition) throws BeansException {
        // 使用构造器创建毛坯bean
        Object obj = createBeanInstance(beanDefinition);

        // 把毛坯bean放入二级缓存
        addEarlySingletonObject(beanName, obj);

        try {
            populateBean(beanName, beanDefinition, obj);
            if (obj != null) {
                obj = initializeBean(beanName, obj, beanDefinition);
            }
        } catch (Exception e) {
            throw new BeansException("Instantiation of bean failed, beanName: " + beanName + ", e: " + e);
        }

        // 用 BeanPostProcessor 处理完 bean 的返回值 (bean的代理对象) 代替原本的 bean 实例
        removeSingleton(beanName);
        registerSingleton(beanName, obj);

        return obj;
    }

    /**
     * 为 bean 的属性赋值
     */
    protected void populateBean(String beanName, BeanDefinition beanDefinition, Object obj) {
        PropertyValues pvs = beanDefinition.getPropertyValues();
        applyPropertyValues(beanName, beanDefinition, obj, pvs);
    }

    /**
     *
     */
    protected Object initializeBean(String beanName, Object singleton, BeanDefinition beanDefinition) {
        // 处理 Aware 接口
        invokeAwareMethods(beanName, singleton);

        // 执行 BeanPostProcessor
        //   1. 在初始化之前处理 bean
        singleton = applyBeanPostProcessorsBeforeInitialization(singleton, beanName);
        //   2. 执行初始化方法
        try {
            invokeInitMethods(beanDefinition, singleton);
        } catch (Throwable e) {
            throw new BeansException("Invocation of init method failed: " + beanName);
        }
        //   3. 在初始化之后处理 bean
        applyBeanPostProcessorsAfterInitialization(singleton, beanName);

        return singleton;
    }

    private void invokeAwareMethods(final String beanName, final Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(this);
            }
        }
    }

    private void invokeInitMethods(BeanDefinition beanDefinition, final Object bean) throws Throwable {
        boolean isInitializingBean = (bean instanceof InitializingBean);
        if (isInitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }

        try {
            if (beanDefinition.getInitMethodName() != null && !"".equals(beanDefinition.getInitMethodName())) {
                invokeCustomInitMethod(beanDefinition, bean);
            }
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    protected void invokeCustomInitMethod(BeanDefinition beanDefinition, final Object bean) throws Throwable {
        Method initMethod = bean.getClass().getMethod(beanDefinition.getInitMethodName());
        ReflectionUtils.makeAccessible(initMethod);
        initMethod.invoke(bean);
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws
        BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : this.getBeanPostProcessors()) {
            beanProcessor.setBeanFactory(this);
            result = beanProcessor.postProcessBeforeInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor beanProcessor : this.getBeanPostProcessors()) {
            result = beanProcessor.postProcessAfterInitialization(result, beanName);
            if (result == null) {
                return null;
            }
        }
        return result;
    }

    protected Object createBeanInstance(BeanDefinition beanDefinition) {
        Object obj = null;
        Class<?> clazz;
        Constructor<?> constructor;

        try {
            // 根据类名获取 class 对象
            clazz = Class.forName(beanDefinition.getClassName());

            // 处理构造器参数
            ConstructorArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
            if (constructorArgumentValues != null && !constructorArgumentValues.isEmpty()) {
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

    private void applyPropertyValues(String beanName, BeanDefinition beanDefinition, Object obj, PropertyValues pvs) {
        // 处理成员变量
        if (pvs != null && !pvs.isEmpty()) {
            for (int i = 0; i < pvs.size(); i++) {
                // 对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = pvs.getPropertyValueList().get(i);
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
                    Class<?> clazz = Class.forName(beanDefinition.getClassName());
                    Method method = clazz.getMethod(methodName, paramType);
                    method.invoke(obj, paramValue);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
