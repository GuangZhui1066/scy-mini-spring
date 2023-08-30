package com.minis.aop;

import java.lang.reflect.Method;

/**
 * 方法的匹配规则
 *
 * 每个实现类代表一种规则
 */
public interface MethodMatcher {

    /**
     * 判断某个方法是否匹配这个规则
     */
    boolean matches(Method method, Class<?> targetClass);

}
