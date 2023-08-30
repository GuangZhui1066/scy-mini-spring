package com.minis.aop;

/**
 * 切点
 *
 * 切点定义了通知执行的范围
 * 切点即某种匹配规则，只有满足此规则的方法才会执行通知 (增强)
 */
public interface Pointcut {

    /**
     * 返回该切点的匹配规则
     */
    MethodMatcher getMethodMatcher();

}
