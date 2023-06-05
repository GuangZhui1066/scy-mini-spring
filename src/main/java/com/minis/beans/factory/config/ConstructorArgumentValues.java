package com.minis.beans.factory.config;

import java.util.ArrayList;
import java.util.List;

/**
 * 类的构造器参数列表
 */
public class ConstructorArgumentValues {

    private final List<ConstructorArgumentValue> constructorArgumentValueList = new ArrayList<>();

    public ConstructorArgumentValues() {
    }

    public void addArgumentValue(ConstructorArgumentValue constructorArgumentValue) {
        this.constructorArgumentValueList.add(constructorArgumentValue);
    }

    public ConstructorArgumentValue getIndexedArgumentValue(int index) {
        return this.constructorArgumentValueList.get(index);
    }

    public int getArgumentCount() {
        return (this.constructorArgumentValueList.size());
    }

    public boolean isEmpty() {
        return (this.constructorArgumentValueList.isEmpty());
    }

}
