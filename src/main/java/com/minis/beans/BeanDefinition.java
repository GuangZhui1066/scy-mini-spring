package com.minis.beans;

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
     */
    private boolean lazyInit = false;

    private String scope = SCOPE_SINGLETON;


    /**
     * 类的构造器参数列表
     */
    private ArgumentValues constructorArgumentValues;

    /**
     * 类的成员变量列表
     */
    private PropertyValues propertyValues;


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

    public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    public ArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

}
