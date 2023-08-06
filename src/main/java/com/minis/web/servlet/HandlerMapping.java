package com.minis.web.servlet;

import javax.servlet.http.HttpServletRequest;

/**
 * 根据 URL 找到对应的处理类及处理方法
 */
public interface HandlerMapping {

    HandlerMethod getHandler(HttpServletRequest request) throws Exception;

}
