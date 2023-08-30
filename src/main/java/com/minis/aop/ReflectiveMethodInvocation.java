package com.minis.aop;

import java.lang.reflect.Method;

/**
 * 通过反射实现方法调用
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    /**
     * 方法所属的对象
     */
    protected final Object target;

    /**
     * 方法本身
     */
    protected final Method method;

    /**
     * 方法参数
     */
    protected Object[] arguments;

    protected ReflectiveMethodInvocation(Object target, Method method,  Object[] arguments) {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }


    @Override
    public Object proceed() throws Throwable {
        return this.method.invoke(this.target, this.arguments);
    }

    @Override
    public final Object getThis() {
        return this.target;
    }

    @Override
    public final Method getMethod() {
        return this.method;
    }

    @Override
    public final Object[] getArguments() {
        return this.arguments;
    }

}
