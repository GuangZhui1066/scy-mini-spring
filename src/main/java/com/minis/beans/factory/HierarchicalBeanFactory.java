package com.minis.beans.factory;

/**
 * 具有继承关系的 BeanFactory
 */
public interface HierarchicalBeanFactory extends BeanFactory {

    /**
     * 获取父级 BeanFactory
     */
    //BeanFactory getParentBeanFactory();

}
