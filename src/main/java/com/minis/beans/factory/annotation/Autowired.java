package com.minis.beans.factory.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自动装配注解
 *
 * 作用：
 *   使用此注解可以将一个 bean 注入到其他 bean 的成员变量中，就不需要在 bean.xml 中配置 ref 属性
 *
 * 解释：
 *   @Target(ElementType.FIELD) 表示这个注解修饰的是类的成员变量 (属性)
 *   @Retention(RetentionPolicy.RUNTIME) 表示这个注解在程序运行时生效
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {
}
