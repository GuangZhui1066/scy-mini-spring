package com.minis.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DispatcherServlet extends HttpServlet {

    /**
     * 记录需要扫描的包名
     */
    private List<String> packageNames = new ArrayList<>();

    /**
     * 记录所有 controller 的名称
     */
    private List<String> controllerNames = new ArrayList<>();

    /**
     * 记录 controller 名称与 controller 实例的映射关系
     */
    private Map<String, Object> controllerObjs = new HashMap<>();

    /**
     * 记录 controller 名称与 controller 类型的映射关系
     */
    private Map<String, Class<?>> controllerClasses = new HashMap<>();

    /**
     * 记录需要处理的 URL
     */
    private List<String> urlMappingNames = new ArrayList<>();

    /**
     * 记录 URL 和处理其的 controller 的映射关系
     */
    private Map<String, Object> mappingObjs = new HashMap<>();

    /**
     *  记录 URL 和处理其的方法的映射关系
     */
    private Map<String, Method> mappingMethods = new HashMap<>();


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

        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        refresh();
    }

    protected void refresh() {
        initController();
        System.out.println(this.controllerNames);
        initMapping();
    }

    protected void initController() {
        // 从扫描的包中获取所有 controller 的名称
        this.controllerNames = scanPackages(this.packageNames);
        // 实例化所有 controller
        for (String controllerName : this.controllerNames) {
            Object obj;
            Class<?> clz = null;
            try {
                clz = Class.forName(controllerName);
                this.controllerClasses.put(controllerName, clz);
            } catch (Exception ignored) {
            }
            try {
                obj = clz.newInstance();
                this.controllerObjs.put(controllerName, obj);
            } catch (Exception ignored) {
            }
        }
    }

    protected void initMapping() {
        for (String controllerName : this.controllerNames) {
            Class<?> clazz = this.controllerClasses.get(controllerName);
            Object obj = this.controllerObjs.get(controllerName);
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                // 找到 controller 中被 @RequestMapping 修饰的方法
                boolean isRequestMapping = method.isAnnotationPresent(RequestMapping.class);
                if (isRequestMapping) {
                    // 建立 URL 与方法名的映射
                    String urlMapping = method.getAnnotation(RequestMapping.class).value();
                    this.urlMappingNames.add(urlMapping);
                    this.mappingObjs.put(urlMapping, obj);
                    this.mappingMethods.put(urlMapping, method);
                }
            }
        }
    }

    private List<String> scanPackages(List<String> packageNames) {
        List<String> tempControllerNames = new ArrayList<>();
        for (String packageName : packageNames) {
            tempControllerNames.addAll(scanPackage(packageName));
        }
        return tempControllerNames;
    }

    private List<String> scanPackage(String packageName) {
        System.out.println("scy test1: " + packageName);
        List<String> tempControllerNames = new ArrayList<>();
        URI uri = null;
        // 将以.分隔的包名换成以/分隔的uri
        try {
            uri = this.getClass().getResource("/" + packageName.replaceAll("\\.", "/")).toURI();
            System.out.println("scy test2: " + uri);
        } catch (Exception ignored) {
        }

        File dir = new File(uri);
        // 处理对应的文件目录
        for (File file : dir.listFiles()) {
            // 子目录
            if (file.isDirectory()) {
                tempControllerNames.addAll(scanPackage(packageName + "." + file.getName()));
            }
            // 类文件
            else {
                String controllerName = packageName + "." + file.getName().replace(".class", "");
                System.out.println("scy test3: " + controllerName);
                tempControllerNames.add(controllerName);
            }
        }
        return tempControllerNames;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取请求的 path
        String sPath = request.getServletPath();
        if (!this.urlMappingNames.contains(sPath)) {
            return;
        }

        Object objResult = null;
        try {
            // 获取处理此 path 的 controller 以及对应方法
            Object obj = this.mappingObjs.get(sPath);
            Method method = this.mappingMethods.get(sPath);
            objResult = method.invoke(obj);
        } catch (Exception ignored) {
        }
        response.getWriter().append(objResult.toString());
    }

}
