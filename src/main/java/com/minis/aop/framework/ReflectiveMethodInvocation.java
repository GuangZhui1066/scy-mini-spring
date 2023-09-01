package com.minis.aop.framework;

import java.lang.reflect.Method;

import com.minis.aop.MethodInvocation;

/**
 * 通过反射实现方法调用
 */
public class ReflectiveMethodInvocation implements MethodInvocation {

    /**
     * 代理对象
     */
    protected final Object proxy;

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

    protected ReflectiveMethodInvocation(Object proxy, Object target, Method method, Object[] arguments) {
        this.proxy = proxy;
        this.target = target;
        this.method = method;
        this.arguments = arguments;
    }


    @Override
    public Object proceed() throws Throwable {
        // 这里是对真实对象进行方法调用（执行原本的代码逻辑），增强逻辑在外层进行
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
