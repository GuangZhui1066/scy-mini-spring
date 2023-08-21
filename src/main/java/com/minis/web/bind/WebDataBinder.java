package com.minis.web.bind;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.minis.beans.BeanWrapperImpl;
import com.minis.beans.PropertyEditor;
import com.minis.beans.PropertyValues;
import com.minis.util.WebUtils;

/**
 * 代表了一个参数类对象 (目标对象)
 *
 * 作用：
 *  将 HttpRequest 请求中的字符串参数转换成不同类型的参数，然后绑定到目标对象中
 *  核心方法是 bind()
 */
public class WebDataBinder {

    /**
     * 目标对象
     */
    private Object target;

    /**
     * 参数名
     */
    private String objectName;

    /**
     * 参数类型
     */
    private Class<?> clz;

    /**
     * 包装对象
     */
    private BeanWrapperImpl beanWrapper;


    public WebDataBinder(Object target, String targetName) {
        this.target = target;
        this.objectName = targetName;
        this.clz = this.target.getClass();
        this.beanWrapper = new BeanWrapperImpl(this.target);
    }

    public void bind(HttpServletRequest request) {
        // 把 request 中的参数转换成 PropertyValues
        PropertyValues pvs = assignParameters(request);
        //
        addBindValues(pvs, request);
        // 把 PropertyValues 中的值绑定到目标对象中
        doBind(pvs);
    }

    private PropertyValues assignParameters(HttpServletRequest request) {
        Map<String, Object> map = WebUtils.getParametersStartingWith(request, "");
        return new PropertyValues(map);
    }

    protected void addBindValues(PropertyValues pvs, HttpServletRequest request) {
    }

    private void doBind(PropertyValues pvs) {
        applyPropertyValues(pvs);
    }

    protected void applyPropertyValues(PropertyValues pvs) {
        getPropertyAccessor().setPropertyValues(pvs);
    }

    protected BeanWrapperImpl getPropertyAccessor() {
        return this.beanWrapper;
    }

    public void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor) {
        getPropertyAccessor().registerCustomEditor(requiredType, propertyEditor);
    }

}
