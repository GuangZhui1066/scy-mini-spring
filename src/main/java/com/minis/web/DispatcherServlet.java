package com.minis.web;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    private Map<String, MappingValue> mappingValues;
    private Map<String, Class<?>> mappingClz = new HashMap<>();
    private Map<String, Object> mappingObjs = new HashMap<>();


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        // 获取配置文件路径
        String contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        // 读取并解析 minisMVC-servlet.xml 中配置的 bean
        Resource rs = new ClassPathXmlResource(xmlPath);
        XmlConfigReader reader = new XmlConfigReader();
        mappingValues = reader.loadConfig(rs);
        refresh();
    }

    /**
     * 对所有的 mappingValues 中注册的类进行实例化
     */
    protected void refresh() {
        for (Map.Entry<String,MappingValue> entry : mappingValues.entrySet()) {
            String id = entry.getKey();
            String className = entry.getValue().getClz();
            Object obj = null;
            Class<?> clz = null;
            try {
                clz = Class.forName(className);
                obj = clz.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mappingClz.put(id, clz);
            mappingObjs.put(id, obj);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求的 path
        String sPath = request.getServletPath();
        if (this.mappingValues.get(sPath) == null) {
            return;
        }

        // 获取处理此 path 的 bean 的类定义、实例、方法名
        Class<?> clz = this.mappingClz.get(sPath);
        Object obj = this.mappingObjs.get(sPath);
        String methodName = this.mappingValues.get(sPath).getMethod();
        Object objResult = null;
        try {
            Method method = clz.getMethod(methodName);
            // 调用 bean 中的方法处理请求
            objResult = method.invoke(obj);
        } catch (Exception ignored) {
        }

        // 将方法返回值写入 response
        response.getWriter().append(objResult.toString());
    }

}
