package com.minis.web.servlet;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.minis.beans.BeansException;
import com.minis.web.RequestMapping;
import com.minis.web.WebApplicationContext;

/**
 * HandlerMapping 的实现类
 *
 * 作用：
 *   根据请求的 URL 找到对应的处理类、处理方法
 */
public class RequestMappingHandlerMapping implements HandlerMapping {

    private WebApplicationContext wac;

    private final MappingRegistry mappingRegistry = new MappingRegistry();

    public RequestMappingHandlerMapping(WebApplicationContext wac) {
        this.wac = wac;
        initMapping();
    }

    protected void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.wac.getBeanDefinitionNames();
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                obj = this.wac.getBean(controllerName);
            } catch (BeansException e) {
                e.printStackTrace();
            }

            Method[] methods = clz.getDeclaredMethods();
            if (methods != null) {
                for(Method method : methods){
                    boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                    if (isRequestMapping) {
                        String urlmapping = method.getAnnotation(RequestMapping.class).value();
                        this.mappingRegistry.getUrlMappingNames().add(urlmapping);
                        this.mappingRegistry.getMappingObjs().put(urlmapping, obj);
                        this.mappingRegistry.getMappingMethods().put(urlmapping, method);
                    }
                }
            }
        }
    }


    @Override
    public HandlerMethod getHandler(HttpServletRequest request) {
        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }

        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        HandlerMethod handlerMethod = new HandlerMethod(method, obj);
        return handlerMethod;
    }

}