package com.minis.beans;

public class BeanDefinition {

    private String name;

    private String className;

    public BeanDefinition(String name, String className) {
        this.name = name;
        this.className = className;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

}