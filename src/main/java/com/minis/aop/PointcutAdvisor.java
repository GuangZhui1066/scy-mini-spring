package com.minis.aop;

/**
 * 支持切点的通知者
 */
public interface PointcutAdvisor extends Advisor {

    Pointcut getPointcut();

}
