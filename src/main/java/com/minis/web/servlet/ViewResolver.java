package com.minis.web.servlet;

/**
 * 视图解析器
 *
 * 作用：
 *   根据视图名找到对应的视图 (View) 实现
 */
public interface ViewResolver {

    View resolveViewName(String viewName) throws Exception;

}
