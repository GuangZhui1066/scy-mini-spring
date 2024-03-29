package com.minis.aop.support;

import java.lang.reflect.Method;

import com.minis.aop.MethodMatcher;
import com.minis.aop.Pointcut;
import com.minis.util.PatternMatchUtils;

/**
 * 切点：根据指定表达式匹配方法名
 */
public class NameMatchMethodPointcut implements MethodMatcher, Pointcut {

    private String mappedName = "";

    public void setMappedName(String mappedName) {
        this.mappedName = mappedName;
    }

    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        if (mappedName.equals(method.getName()) || isMatch(method.getName(), mappedName)) {
            return true;
        }
        return false;
    }

    protected boolean isMatch(String methodName, String mappedName) {
        return PatternMatchUtils.simpleMatch(mappedName, methodName);
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }

}
