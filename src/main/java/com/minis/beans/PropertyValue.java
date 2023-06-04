package com.minis.beans;

/**
 * 类的成员变量
 */
public class PropertyValue {

    private final String name;
    private final String type;
    private final Object value;

    public PropertyValue(String name, String type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

}
