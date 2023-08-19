package com.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;

import com.minis.web.method.HandlerMethod;

/**
 * 根据 URL 找到对应的处理类及处理方法
 */
public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;

}
