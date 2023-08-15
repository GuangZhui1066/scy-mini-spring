package com.minis.beans;

/**
 * 类的成员变量
 */
public class PropertyValue {

    private final String name;
    private final String type;
    private final Object value;
    private final boolean isRef;

    public PropertyValue(String name, String type, Object value, boolean isRef) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.isRef = isRef;
    }

    public PropertyValue(String name, Object value) {
        this(name, "", value, false);
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

    public boolean isRef() {
        return isRef;
    }
}
