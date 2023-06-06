package com.minis.beans.factory.config;

import com.minis.beans.PropertyValues;

public class BeanDefinition {

    /**
     * 单例模式
     */
    String SCOPE_SINGLETON = "singleton";

    /**
     * 原型模式
     */
    String SCOPE_PROTOTYPE = "prototype";

    /**
     * 是否在加载的时候初始化
     *   true:
     *   false: 在加载的时候就要初始化
     *
     * 这里需要设置为 true，否则在注册 scyTestService 的 beanDefinition 的时候就会实例化 scyTestService 对象
     * 实例化 scyTestService 对象时就需要实例化其成员变量 scyBaseService，但此时 scyBaseService 的 beanDefinition 还没有被注册，所以会报错
     * 因为 bean.xml 中 scyTestService 的定义在 scyBaseService 之前，XmlBeanDefinitionReader 是按顺序注册 beanDefinition 的
     * 将 lazyInit 设置为 true 后，Spring就会先将 bean.xml 中定义的所有 bean 的 beanDefinition 注册好，然后使用 bean 时再去实例化 bean
     */
    private boolean lazyInit = true;

    private String scope = SCOPE_SINGLETON;


    /**
     * 类的构造器参数列表
     */
    private ConstructorArgumentValues constructorArgumentValues;

    /**
     * 类的成员变量列表
     */
    private PropertyValues propertyValues;

    /**
     * 依赖的bean
     */
    private String[] dependsOn;

    /**
     * bean在初始化时要执行的方法的方法名
     *
     * 注意：
     *   1. init 方法不是构造方法，其在构造器创建出对象之后才执行
     *   2. init 方法没有参数，且必须是 public 的
     *   3. init 方法的方法名可以自定义
     */
    private String initMethodName;


    private String name;

    private String className;

    public BeanDefinition(String name, String className) {
        this.name = name;
        this.className = className;
    }


    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public boolean isSingleton() {
        return SCOPE_SINGLETON.equals(scope);
    }

    public boolean isPrototype() {
        return SCOPE_PROTOTYPE.equals(scope);
    }

    public void setConstructorArgumentValues(ConstructorArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public ConstructorArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    public String[] getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    public String getInitMethodName() {
        return initMethodName;
    }

    public void setInitMethodName(String initMethodName) {
        this.initMethodName = initMethodName;
    }

}
