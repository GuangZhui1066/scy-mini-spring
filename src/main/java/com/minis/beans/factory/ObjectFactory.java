package com.minis.beans.factory;

import com.minis.beans.BeansException;

/**
 * 对象工厂
 */
public interface ObjectFactory<T> {

    /**
     * Return an instance (possibly shared or independent)
     * of the object managed by this factory.
     */
    T getObject() throws BeansException;

}
