package com.minis.web.servlet.view;

import com.minis.web.servlet.View;
import com.minis.web.servlet.ViewResolver;

/**
 * 用于解析 JSP 视图的视图解析器
 */
public class InternalResourceViewResolver implements ViewResolver {

    private Class<?> viewClass = null;

    private String viewClassName = "";

    private String contentType;

    private String prefix = "";
    private String suffix = "";

    public InternalResourceViewResolver() {
        if (getViewClass() == null) {
            setViewClass(JstlView.class);
        }
    }

    public void setViewClassName(String viewClassName) {
        this.viewClassName = viewClassName;
        Class<?> clz = null;
        try {
            clz = Class.forName(viewClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setViewClass(clz);
    }

    protected String getViewClassName() {
        return this.viewClassName;
    }
    public void setViewClass(Class<?> viewClass) {
        this.viewClass = viewClass;
    }
    protected Class<?> getViewClass() {
        return this.viewClass;
    }

    public void setPrefix(String prefix) {
        this.prefix = (prefix != null ? prefix : "");
    }

    protected String getPrefix() {
        return this.prefix;
    }

    public void setSuffix(String suffix) {
        this.suffix = (suffix != null ? suffix : "");
    }

    protected String getSuffix() {
        return this.suffix;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    protected String getContentType() {
        return this.contentType;
    }


    @Override
    public View resolveViewName(String viewName) throws Exception {
        return buildView(viewName);
    }

    protected View buildView(String viewName) throws Exception {
        // 获取视图的实现类型
        Class<?> viewClass = this.getViewClass();

        // 创建一个此类型的视图实例
        View view = (View) viewClass.newInstance();
        view.setUrl(this.getPrefix() + viewName + this.getSuffix());
        view.setContentType(this.getContentType());

        // 返回此视图实例
        return view;
    }

}
