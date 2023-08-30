package com.minis.aop;

import java.lang.reflect.Method;

/**
 * 对方法调用过程的包装
 */
public interface MethodInvocation {

    /**
     * 获取方法本身，即 Method 对象
     */
    Method getMethod();

    /**
     * 获取方法参数
     */
    Object[] getArguments();

    /**
     * 获取方法所属对象，即是哪个对象要执行这个方法
     */
    Object getThis();

    /**
     * 执行方法，返回方法的执行结果
     */
    Object proceed() throws Throwable;

}
