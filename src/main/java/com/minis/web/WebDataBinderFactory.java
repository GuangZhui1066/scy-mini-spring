package com.minis.web;

import javax.servlet.http.HttpServletRequest;

/**
 * WebDataBinder 的工厂类，用于操作 WebDataBinder
 */
public class WebDataBinderFactory {

    public WebDataBinder createBinder(HttpServletRequest request, Object target, String objectName) {
        WebDataBinder webDataBinder = new WebDataBinder(target, objectName);
        initBinder(webDataBinder, request);
        return webDataBinder;
    }

    protected void initBinder(WebDataBinder dataBinder, HttpServletRequest request){
    }

}
