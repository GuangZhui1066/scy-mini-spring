package com.minis.web.servlet;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.web.WebApplicationContext;
import com.minis.web.WebBindingInitializer;
import com.minis.web.WebDataBinder;
import com.minis.web.WebDataBinderFactory;

/**
 * HandlerAdapter 的实现类
 *
 * 作用：
 *   根据请求的 URL 调用对应的处理类、处理方法
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {

    private WebApplicationContext wac;

    private WebBindingInitializer webBindingInitializer = null;

    public RequestMappingHandlerAdapter(WebApplicationContext wac) {
        this.wac = wac;
        try {
            this.webBindingInitializer = (WebBindingInitializer)this.wac.getBean("webBindingInitializer");
        } catch (BeansException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        handleInternal(request, response, (HandlerMethod) handler);
    }

    private void handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        try {
            invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用请求对应的处理方法
     */
    protected void invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        // 要调用的方法的参数列表
        Parameter[] methodParameters = handlerMethod.getMethod().getParameters();

        // 调用方法时需要传入的参数实例
        Object[] methodParamObjs = new Object[methodParameters.length];

        // 对调用方法里的每一个参数，进行属性值绑定
        WebDataBinderFactory binderFactory = new WebDataBinderFactory();
        int i = 0;
        for (Parameter methodParameter : methodParameters) {
            // 创建参数实例
            // todo: 如果参数类型是 Double, int 等，因为其没有无参构造方法，所以不能用 newInstance() 方法创建实例，会抛 InstantiationException 实例化异常
            Object methodParamObj = methodParameter.getType().newInstance();
            WebDataBinder wdb = binderFactory.createBinder(request, methodParamObj, methodParameter.getName());
            webBindingInitializer.initBinder(wdb);
            wdb.bind(request);
            methodParamObjs[i] = methodParamObj;
            i++;
        }

        // 传入参数，调用处理方法
        Method invocableMethod = handlerMethod.getMethod();
        Object returnObj = invocableMethod.invoke(handlerMethod.getBean(), methodParamObjs);

        response.getWriter().append(returnObj.toString());
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

}
