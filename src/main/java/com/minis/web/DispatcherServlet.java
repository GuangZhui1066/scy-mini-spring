package com.minis.web;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.minis.beans.BeansException;
import com.minis.web.servlet.HandlerAdapter;
import com.minis.web.servlet.HandlerMapping;
import com.minis.web.servlet.HandlerMethod;
import com.minis.web.servlet.RequestMappingHandlerAdapter;
import com.minis.web.servlet.RequestMappingHandlerMapping;

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

    private HandlerMapping handlerMapping;
    private HandlerAdapter handlerAdapter;

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

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

        initHandlerMappings(this.webApplicationContext);
        initHandlerAdapters(this.webApplicationContext);
        initViewResolvers(this.webApplicationContext);
    }

    protected void initController() {
        this.controllerNames = Arrays.asList(this.webApplicationContext.getBeanDefinitionNames());
        for (String controllerName : this.controllerNames) {
            try {
                this.controllerClasses.put(controllerName, Class.forName(controllerName));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                this.controllerObjs.put(controllerName, this.webApplicationContext.getBean(controllerName));
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    protected void initHandlerMappings(WebApplicationContext wac) {
        this.handlerMapping = new RequestMappingHandlerMapping(wac);

    }
    protected void initHandlerAdapters(WebApplicationContext wac) {
        this.handlerAdapter = new RequestMappingHandlerAdapter(wac);
    }

    protected void initViewResolvers(WebApplicationContext wac) {
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) {
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);

        try {
            doDispatch(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerMethod handlerMethod = this.handlerMapping.getHandler(request);
        if (handlerMethod == null) {
            return;
        }

        HandlerAdapter handlerAdapter = this.handlerAdapter;
        handlerAdapter.handle(request, response, handlerMethod);
    }

}
