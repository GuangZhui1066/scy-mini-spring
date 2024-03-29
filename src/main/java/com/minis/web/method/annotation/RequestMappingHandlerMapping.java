package com.minis.web.method.annotation;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import com.minis.beans.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.web.bind.annotation.RequestMapping;
import com.minis.web.method.HandlerMethod;
import com.minis.web.servlet.HandlerMapping;

/**
 * HandlerMapping 的实现类
 *
 * 作用：
 *   根据请求的 URL 找到对应的处理类、处理方法
 */
public class RequestMappingHandlerMapping implements HandlerMapping, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private MappingRegistry mappingRegistry = null;

    public RequestMappingHandlerMapping() {
    }

    protected void initMapping() {
        Class<?> clz = null;
        Object obj = null;
        String[] controllerNames = this.applicationContext.getBeanDefinitionNames();
        for (String controllerName : controllerNames) {
            try {
                clz = Class.forName(controllerName);
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }
            try {
                obj = this.applicationContext.getBean(controllerName);
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
        if (this.mappingRegistry == null) {
            this.mappingRegistry = new MappingRegistry();
            initMapping();
        }

        String sPath = request.getServletPath();
        if (!this.mappingRegistry.getUrlMappingNames().contains(sPath)) {
            return null;
        }

        // 根据请求类型 GET / POST 匹配对应的调用方法
        Method method = this.mappingRegistry.getMappingMethods().get(sPath);
        Object obj = this.mappingRegistry.getMappingObjs().get(sPath);
        HandlerMethod handlerMethod = new HandlerMethod(method, obj);
        return handlerMethod;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
