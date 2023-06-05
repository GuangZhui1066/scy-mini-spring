package com.minis.beans.factory.config;

/**
 * 类的构造器参数
 */
public class ConstructorArgumentValue {

    private String name;
    private String type;
    private Object value;

    public ConstructorArgumentValue(String type, Object value) {
        this.type = type;
        this.value = value;
    }
    public ConstructorArgumentValue( String name, String type, Object value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return this.value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
