package com.minis.web.servlet;

import java.lang.reflect.Method;

/**
 * 处理特定URL请求的 处理类及处理方法
 */
public class HandlerMethod {

    private Object bean;
    private Method method;

    public HandlerMethod(Method method, Object obj) {
        this.setMethod(method);
        this.setBean(obj);

    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

}

