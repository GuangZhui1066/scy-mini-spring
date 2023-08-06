package com.minis.web;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
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

import com.minis.beans.BeansException;
import com.minis.beans.factory.annotation.Autowired;

/**
 * Servlet 控制器
 *
 * 作用：
 *   加载配置文件 (minisMVC-servlet.xml) 中的 servlet，并规定servlet拦截的所有 HTTP 请求
 *
 * 注：
 *   Service 类由 Listener 创建 (Listener初始化时会创建 IoC 容器，由IoC容器实例化Service类)
 *   Controller 类由 DispatchServlet 创建
 */
public class DispatcherServlet extends HttpServlet {

    // 父级上下文，负责 (在Listener初始化时) 创建 Service 类的实例
    private WebApplicationContext parentApplicationContext;

    // 子级上下文，负责 (在Servlet初始化时) 创建 Controller 类的实例
    private WebApplicationContext webApplicationContext;

    private String contextConfigLocation;

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

        this.parentApplicationContext = (WebApplicationContext) this.getServletContext().
            getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);

        // 获取配置 servlet 的配置文件 (即定义 Controller 的 minisMVC-servlet.xml 文件) 路径
        this.contextConfigLocation = config.getInitParameter("contextConfigLocation");
        URL xmlPath = null;
        try {
            xmlPath = this.getServletContext().getResource(contextConfigLocation);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        this.packageNames = XmlScanComponentHelper.getNodeValue(xmlPath);

        // 创建子级上下文，负责实例化 Controller
        this.webApplicationContext = new AnnotationConfigWebApplicationContext(contextConfigLocation, parentApplicationContext);

        // 加载 bean
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

                populateBean(obj, controllerName);

                this.controllerObjs.put(controllerName, obj);
            } catch (Exception ignored) {
            }
        }
    }

    protected Object populateBean(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            for (Field field : fields) {
                boolean isAutowired = field.isAnnotationPresent(Autowired.class);
                if (isAutowired) {
                    String fieldName = field.getName();
                    Object autowiredObj = this.webApplicationContext.getBean(fieldName);
                    try {
                        field.setAccessible(true);
                        field.set(bean, autowiredObj);
                    } catch (IllegalArgumentException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bean;
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
