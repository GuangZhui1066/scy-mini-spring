package com.minis.web;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyEditorRegistrySupport;
import com.minis.beans.PropertyValue;
import com.minis.beans.PropertyValues;

/**
 * bean 的包装类
 *
 * 作用：
 *  把 bean 的属性值绑定到 bean (目标对象)上
 */
public class BeanWrapperImpl extends PropertyEditorRegistrySupport {

    /**
     * 目标对象
     */
    Object wrappedObject;

    /**
     * 目标对象的类型
     */
    Class<?> clz;

    /**
     * bean 的属性值
     */
    PropertyValues pvs;

    public BeanWrapperImpl(Object object) {
        // 不同数据类型的参数转换器editor
        registerDefaultEditors();
        this.wrappedObject = object;
        this.clz = object.getClass();
    }

    public void setBeanInstance(Object object) {
        this.wrappedObject = object;
    }

    public Object getBeanInstance() {
        return wrappedObject;
    }

    /**
     * 把 bean 的属性值绑定到 bean 上
     */
    public void setPropertyValues(PropertyValues pvs) {
        this.pvs = pvs;
        for (PropertyValue pv : this.pvs.getPropertyValues()) {
            setPropertyValue(pv);
        }
    }

    /**
     * 为某个具体的属性绑定值
     */
    public void setPropertyValue(PropertyValue pv) {
        // 拿到该属性的处理器
        BeanPropertyHandler propertyHandler = new BeanPropertyHandler(pv.getName());

        // 拿到该属性类型的转换器
        // 先尝试获取自定义转换器，没有对应类型的自定义转换器再去获取默认转换器
        PropertyEditor pe = this.getCustomEditor(propertyHandler.getPropertyClz());
        if (pe == null) {
            pe = this.getDefaultEditor(propertyHandler.getPropertyClz());
        }

        // 设置该属性的值
        pe.setAsText((String) pv.getValue());
        propertyHandler.setValue(pe.getValue());
    }


    /**
     * Bean 的属性值处理器
     * 用于对 bean 的属性进行 get / set 操作
     */
    class BeanPropertyHandler {

        Method writeMethod = null;
        Method readMethod = null;
        Class<?> propertyClz = null;

        public Class<?> getPropertyClz() {
            return propertyClz;
        }

        public BeanPropertyHandler(String propertyName) {
            try {
                // 获取参数对应的属性及类型
                Field field = clz.getDeclaredField(propertyName);
                propertyClz = field.getType();
                // 获取该属性的 set 方法 (默认方法名为 set + 字段名，有一个参数)
                this.writeMethod = clz.getDeclaredMethod("set" +
                    propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1), propertyClz);
                // 获取该属性的 get 方法 (默认方法名为 get + 字段名，没有参数)
                this.readMethod = clz.getDeclaredMethod("get" +
                    propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 调用 get 方法
        public Object getValue() {
            Object result = null;
            writeMethod.setAccessible(true);
            try {
                result = readMethod.invoke(wrappedObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        }

        // 调用 set 方法
        public void setValue(Object value) {
            writeMethod.setAccessible(true);
            try {
                writeMethod.invoke(wrappedObject, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
