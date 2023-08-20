package com.minis.web.method.annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.context.ApplicationContext;
import com.minis.context.ApplicationContextAware;
import com.minis.http.converter.HttpMessageConverter;
import com.minis.web.bind.WebDataBinder;
import com.minis.web.bind.annotation.ResponseBody;
import com.minis.web.bind.support.WebBindingInitializer;
import com.minis.web.bind.support.WebDataBinderFactory;
import com.minis.web.method.HandlerMethod;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.ModelAndView;

/**
 * HandlerAdapter 的实现类
 *
 * 作用：
 *   根据请求的 URL 调用对应的处理类、处理方法
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, ApplicationContextAware {

    private ApplicationContext applicationContext;

    private WebBindingInitializer webBindingInitializer = null;

    private HttpMessageConverter messageConverter = null;

    public RequestMappingHandlerAdapter() {
    }


    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return handleInternal(request, response, (HandlerMethod) handler);
    }

    private ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler) {
        ModelAndView mav = null;

        try {
            mav = invokeHandlerMethod(request, response, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return mav;
    }

    /**
     * 调用请求对应的处理方法
     */
    protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
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

        ModelAndView mav = null;
        // 如果返回带有 @ResponseBody 注解，直接把返回对象格式化为 (JSON) 字符串写到 response
        if (invocableMethod.isAnnotationPresent(ResponseBody.class)) {
            this.messageConverter.write(returnObj, response);
        }
        // 返回的是前端页面，把返回结果包装成 ModelAndView 对象返回
        else {
            if (returnObj instanceof ModelAndView) {
                mav = (ModelAndView) returnObj;
            }
            else if(returnObj instanceof String) {
                String sTarget = (String) returnObj;
                mav = new ModelAndView();
                mav.setViewName(sTarget);
            }
        }

        return mav;
    }

    public WebBindingInitializer getWebBindingInitializer() {
        return webBindingInitializer;
    }

    public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer) {
        this.webBindingInitializer = webBindingInitializer;
    }

    public HttpMessageConverter getMessageConverter() {
        return messageConverter;
    }

    public void setMessageConverter(HttpMessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

}
