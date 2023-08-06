package com.minis.web.servlet;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 存储 URL 以及对应的处理类、处理方法的映射关系
 */
public class MappingRegistry {

    /**
     * 将原来 DispatcherServlet 中的相关成员重构出来
     *  urlMappingNames: 记录需要处理的 URL
     *  mappingObjs: 记录 URL 和处理其的 controller 的映射关系
     *  mappingMethods: 记录 URL 和处理其的方法的映射关系
     */
    private List<String> urlMappingNames = new ArrayList<>();
    private Map<String,Object> mappingObjs = new HashMap<>();
    private Map<String, Method> mappingMethods = new HashMap<>();

    public List<String> getUrlMappingNames() {
        return urlMappingNames;
    }

    public void setUrlMappingNames(List<String> urlMappingNames) {
        this.urlMappingNames = urlMappingNames;
    }

    public Map<String,Object> getMappingObjs() {
        return mappingObjs;
    }

    public void setMappingObjs(Map<String,Object> mappingObjs) {
        this.mappingObjs = mappingObjs;
    }

    public Map<String,Method> getMappingMethods() {
        return mappingMethods;
    }

    public void setMappingMethods(Map<String,Method> mappingMethods) {
        this.mappingMethods = mappingMethods;
    }

}

